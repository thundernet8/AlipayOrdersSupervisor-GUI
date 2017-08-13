package com.wxq.apsv.model;

import com.wxq.apsv.enums.TaskStatus;

public class ApsvTask {
    public int id;

    public String name;

    // 任务备注
    public String note;

    // 抓取任务需要对应Alipay cookies
    public String cookie;

    // 抓取的订单推送API地址
    public String pushApi;

    // 与推送API地址协议的secret,以校验数据来源合法性
    public String pushSecret;

    // 任务执行状态
    public TaskStatus status = TaskStatus.STOPPED;

    public ApsvTask() {

    }
}