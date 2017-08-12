package com.wxq.apsv.model;

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

    // 抓取订单时间间隔(s)
    public int interval = 60;

    // 任务执行状态
    public boolean running = false;

    // 任务执行开始时间
    public int startTime = 0;

    // 任务执行并自动退出的时间(s), 0则表示无限运行直到手动终止
    public int executeTime = 0;

    public ApsvTask() {

    }
}