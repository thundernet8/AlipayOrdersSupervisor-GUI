package com.wxq.apsv.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 用于保存具体任务下已成功推送的tradeNo，以避免重复推送
 */
public final class PushData {
    private static Map<String, Set<String>> successOrderPushMap = new HashMap<>();

    synchronized public static void AddSuccessRecord(int taskId, String tradeNo) {
        Set<String> taskTradeNos = successOrderPushMap.get(Integer.toString(taskId));
        if (taskTradeNos == null) {
            taskTradeNos = new HashSet<>();
        }
        taskTradeNos.add(tradeNo);
        successOrderPushMap.put(Integer.toString(taskId), taskTradeNos);
    }

    public static boolean IsTradeNoHandled(int taskId, String tradeNo) {
        Set<String> taskTradeNos = successOrderPushMap.get(Integer.toString(taskId));
        if (taskTradeNos == null) {
            return false;
        }
        return taskTradeNos.contains(tradeNo);
    }
}
