package com.wxq.apsv.worker;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public final class ApsvTimerManager {
    private static Map<String, Timer> timerMap = new HashMap<>();

    public static void AddTimer(Timer timer, int taskId) {
        timerMap.put(Integer.toString(taskId), timer);
    }

    public static Timer GetTimer(int taskId) {
        return timerMap.get(Integer.toString(taskId));
    }
}