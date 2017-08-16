package com.wxq.apsv.model;

import com.wxq.apsv.enums.TaskStatus;

public final class ApsvTaskStatus {
    public static final String toString(TaskStatus status) {
        switch (status) {
            case STOPPED:
                return "未运行";
            case RUNNING:
                return "运行中";
            case INERROR:
                return "异常";
        }
        return "";
    }
}