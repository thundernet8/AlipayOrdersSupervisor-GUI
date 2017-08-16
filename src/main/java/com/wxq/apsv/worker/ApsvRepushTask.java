package com.wxq.apsv.worker;

import com.wxq.apsv.model.ApsvOrder;
import com.wxq.apsv.model.ApsvTask;
import com.wxq.apsv.model.PushData;
import com.wxq.apsv.model.RunTasksModel;
import com.wxq.apsv.utils.HttpRequest;
import com.wxq.apsv.utils.Settings;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.TimerTask;

public class ApsvRepushTask extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(ApsvTimerTask.class);

    private ApsvTask task;

    private ApsvOrder order;

    public ApsvRepushTask(ApsvTask task, ApsvOrder order) {
        this.task = task;
        this.order = order;
    }

    @Override
    public void run() {
        logger.info("ApsvRepushTask run at Thread {}", Thread.currentThread().getId());
        if (task == null || order == null || task.id != order.taskId) {
            return;
        }
        // 查询是否之前已经推送成功了
        if (PushData.IsTradeNumHandled(task.id, order.tradeNo)) {
            order.pushed = true;
        } else {
            // 需要推送
            HashMap data = new HashMap<String, Object>();
            data.put("tradeNo", order.tradeNo);
            data.put("time", order.time);
            data.put("description", order.description);
            data.put("memo", order.memo);
            data.put("username", order.username);
            data.put("amount", order.amount);
            data.put("status", order.status);
            data.put("sig", order.sig);
            data.put("version", order.version);
            String code = HttpRequest.DoPost(task.pushApi, "", data);
            logger.info("RePush order {} with response body {}", order.tradeNo, code);
            if (StringUtils.equals(code, Settings.getInstance().getPushSuccessBody())) {
                order.pushed = true;
                // 保存到已推送成功列表
                PushData.AddSuccessRecord(task.id, order.tradeNo);
            } else {
                order.pushed = false;
            }
        }

        RunTasksModel runTasksModel = RunTasksModel.getInstance();
        logger.info("Add order with tradeNo: {}", order.tradeNo);
        runTasksModel.AddOrder(order);
    }
}
