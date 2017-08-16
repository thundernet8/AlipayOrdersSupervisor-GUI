package com.wxq.apsv.model;

public class ApsvOrder {
    public int taskId;

    public String tradeNo;

    public String time;

    public String description;

    public String memo;

    public String username;

    public Float amount;

    public String status;

    // 其他程序运行附带信息
    public boolean pushed = false;

    // md5一些关键信息的hash值，提供给服务端校验
    public String sig;

    // 标记应用API数据版本以便服务端对应处理
    public String version = "1.0";
}
