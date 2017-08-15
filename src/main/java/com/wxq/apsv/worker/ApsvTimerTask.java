package com.wxq.apsv.worker;

import com.wxq.apsv.model.ApsvOrder;
import com.wxq.apsv.model.ApsvTask;
import com.wxq.apsv.model.RunTasksModel;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
        ArrayList<ApsvOrder> orders = findOrders(getPage());

        RunTasksModel runTasksModel = RunTasksModel.getInstance();
        orders.forEach(o -> {
            logger.info("Add order with tradeNo: {}", o.tradeNo);
            runTasksModel.AddOrder(o);
        });
    }

    private String getPage() {
        // TODO
        return "";
    }

    private ArrayList<ApsvOrder> findOrders(String html) {
        // TODO
        ArrayList<ApsvOrder> orders = new ArrayList<>();
        orders.add(new ApsvOrder(){
            {
                taskId = task.id;
                datetime = "2017-8-8";
                tradeNo = "888888";
                username = "wuxueqian";
                amount = 1.0f;
                status = "交易成功";
                memo = "memo";
            }
        });
        return orders;
    }
}
