package com.wxq.apsv.worker;

import com.wxq.apsv.model.ApsvOrder;
import com.wxq.apsv.model.ApsvTask;
import com.wxq.apsv.model.Constants;
import com.wxq.apsv.model.RunTasksModel;
import com.wxq.apsv.utils.HttpRequest;
import com.wxq.apsv.utils.Order;
import org.apache.commons.lang.StringUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class ApsvTimerTask extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(ApsvTimerTask.class);

    private ApsvTask task;

    public ApsvTimerTask(ApsvTask task) {
        this.task = task;
    }

    @Override
    public void run() {
        logger.info("ApsvTimerTask run at Thread {}", Thread.currentThread().getId());
        if (task == null || StringUtils.isEmpty(task.cookie)) {
            return;
        }
        ArrayList<ApsvOrder> orders = pushOrders(findOrders(getPage()));
        // ArrayList<ApsvOrder> orders = pushOrders(null);

        RunTasksModel runTasksModel = RunTasksModel.getInstance();
        orders.forEach(o -> {
            logger.info("Add order with tradeNo: {}", o.tradeNo);
            runTasksModel.AddOrder(o);
        });
    }

    private String getPage() {
        // 先请求个人主页
        String alipayUserCenterPage = HttpRequest.DoGet(Constants.ALIPAY_UC_URL, task.cookie);
        if (StringUtils.isEmpty(alipayUserCenterPage)) {
            logger.error("Fetch {} failed", Constants.ALIPAY_UC_URL);
            return "";
        }

        String orderListPage = HttpRequest.DoGet(Constants.ALIPAY_ADVANCED_ORDERS_URL, task.cookie);
        if (StringUtils.isEmpty(orderListPage)) {
            logger.error("Fetch {} failed", Constants.ALIPAY_ADVANCED_ORDERS_URL);
        }
        return orderListPage;
    }

    private ArrayList<ApsvOrder> findOrders(String html) {
        logger.info("Html: {}", html);
        //File input = new File("/Users/WXQ/Desktop/orders.html");
        ArrayList<ApsvOrder> orders = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        // test start
//        try {
//            doc = Jsoup.parse(input, "UTF-8", "");
//        } catch (IOException e) {
//
//        }
        // -- test end
        Element ordersForm = doc.getElementById("J-submit-form");
        if (ordersForm == null) {
            logger.error("Cannot find order list form, maybe cookie expires");
            return orders;
        }

        Elements tableBody = doc.select("#tradeRecordsIndex>tbody");
        Elements orderRows = tableBody.select("tr");

        orderRows.forEach(row -> {
            Elements timeNodes = row.select("td.time p");
            String[] orderNoData = row.select("td.tradeNo p").text().split("\\|");
            ApsvOrder order = new ApsvOrder(){
                {
                    taskId = task.id;
                    datetime = timeNodes.get(0).text() + " " + timeNodes.get(timeNodes.size() - 1).text();
                    description = row.select(".memo-info").text();
                    memo = row.select("td.memo p").text();
                    tradeNo = orderNoData.length > 1 ? orderNoData[1].split(":")[1] : orderNoData[0].split(":")[1];
                    username = row.select("td.other p").text();
                    amount = Float.parseFloat(row.select("td.amount span").text().replaceAll("\\s+", ""));
                    status = row.select("td.status p").text();
                }
            };
            order.sig = Order.Sign(order, task.pushSecret);
            orders.add(order);
        });
        return orders;
    }

    private ArrayList<ApsvOrder> pushOrders(ArrayList<ApsvOrder> orders) {
        // TODO
        String s = HttpRequest.DoPost("https://webapproach.net/site/apsvnotify", "", new HashMap<String, Object>());
        return orders;
    }
}
