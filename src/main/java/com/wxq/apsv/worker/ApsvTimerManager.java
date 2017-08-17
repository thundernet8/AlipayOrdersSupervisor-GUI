package com.wxq.apsv.worker;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * 保存各个抓取任务的定时器
 */
public final class ApsvTimerManager {
    private static Map<String, Timer> timerMap = new HashMap<>();

    public static void AddTimer(Timer timer, int taskId) {
        timerMap.put(Integer.toString(taskId), timer);
    }

    public static Timer GetTimer(int taskId) {
        return timerMap.get(Integer.toString(taskId));
    }

    // 任务开始时间戳Map
    private static Map<String, Date> startTimeMap = new HashMap<>();

    public static void RecordStartTime(int taskId) {
        Date date = new Date();
        startTimeMap.put(Integer.toString(taskId), date);
    }

    public static void ClearStartTime(int taskId) {
        startTimeMap.remove(Integer.toString(taskId));
    }

    /**
     * 计算任务的运行时间并格式化为友好的字符串
     * @param taskId int
     * @return String
     */
    public static String GetElapseTime(int taskId) {
        Date date = startTimeMap.get(Integer.toString(taskId));
        if (date == null) {
            return "";
        }
        Date now = new Date();
        long seconds = (now.getTime() - date.getTime()) / 1000;
        if (seconds < 60) {
            return seconds + " 秒";
        } else if (seconds < 3600) {
            return (int)(seconds / 60) + " 分 " + (int)(seconds % 60) + " 秒";
        } else if (seconds < 3600 * 24) {
            int hours = (int)(seconds / 3600);
            int minutes = (int)(seconds % 3600 / 60);
            return hours + " 小时 " + minutes + " 分钟 " + (int)(seconds % 60) + " 秒";
        } else {
            int days = (int)(seconds / 3600 / 24);
            int hours = (int)(seconds % (3600 * 24) / 3600);
            int minutes = (int)(seconds % (3600 * 24) % 3600 / 60);
            return days + " 天 " + hours + " 小时 " + minutes + " 分钟 " + (int)(seconds % 60) + " 秒";
        }
    }
}