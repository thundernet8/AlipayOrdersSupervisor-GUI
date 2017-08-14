package com.wxq.apsv.model;

public class ApsvOrder {
    public int taskId;

    public String tradeNo;

    public String datetime;

    public String memo;

    public String username;

    public Float amount;

    public String status;

    // 其他程序运行附带信息
    public boolean pushed = false;
}
