package com.wxq.apsv.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unicode {
    /**
     * 将支付宝订单用户名包含Unicode编码转换为中文
     * @param str String
     * @return String
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}
