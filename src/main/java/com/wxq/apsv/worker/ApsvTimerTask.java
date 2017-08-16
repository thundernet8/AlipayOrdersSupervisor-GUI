package com.wxq.apsv.worker;

import com.wxq.apsv.model.*;
import com.wxq.apsv.utils.HttpRequest;
import com.wxq.apsv.utils.Order;
import com.wxq.apsv.utils.Settings;
import com.wxq.apsv.utils.Unicode;
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

        RunTasksModel runTasksModel = RunTasksModel.getInstance();
        // 先从html分析订单(较快)
        ArrayList<ApsvOrder> orders = findOrders(getPage());
        orders.forEach(o -> {
            logger.info("Add order(has not push) with tradeNo: {}", o.tradeNo);
            runTasksModel.AddOrder(o);
        });

        orders = pushOrders(orders);
        orders.forEach(o -> {
            logger.info("Add order(pushed yet) with tradeNo: {}", o.tradeNo);
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
        //logger.info("Html: {}", html);
        ArrayList<ApsvOrder> orders = new ArrayList<>();

        Document doc = Jsoup.parse(html);

        Element ordersForm = doc.getElementById("J-submit-form");
        if (ordersForm == null) {
            logger.error("Cannot find order list form, maybe cookie expires");
            // 标记task status为异常
            RunTasksModel.getInstance().MarkTaskException(task.id);
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
                    time = timeNodes.get(0).text() + " " + timeNodes.get(timeNodes.size() - 1).text();
                    description = row.select(".memo-info").text();
                    memo = row.select("td.memo p").text();
                    tradeNo = orderNoData.length > 1 ? orderNoData[1].split(":")[1] : orderNoData[0].split(":")[1];
                    username = Unicode.unicodeToString(row.select("td.other p").text());
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
        ArrayList<ApsvOrder> newOrders = new ArrayList<>();
        orders.forEach(o -> {
            // 查询是否之前已经推送成功了
            if (PushData.IsTradeNumHandled(task.id, o.tradeNo)) {
                o.pushed = true;
                newOrders.add(o);
            } else {
                // 需要推送
                HashMap data = new HashMap<String, Object>();
                data.put("tradeNo", o.tradeNo);
                data.put("time", o.time);
                data.put("description", o.description);
                data.put("memo", o.memo);
                data.put("username", o.username);
                data.put("amount", o.amount);
                data.put("status", o.status);
                data.put("sig", o.sig);
                data.put("version", o.version);
                String code = HttpRequest.DoPost(task.pushApi, "", data);
                logger.info("Push order {} with response body {}", o.tradeNo, code);
                if (StringUtils.equals(code, Settings.getInstance().getPushSuccessBody())) {
                    o.pushed = true;
                    // 保存到已推送成功列表
                    PushData.AddSuccessRecord(o.taskId, o.tradeNo);
                } else {
                    o.pushed = false;
                }
                newOrders.add(o);
            }
        });

        return newOrders;
    }
}
