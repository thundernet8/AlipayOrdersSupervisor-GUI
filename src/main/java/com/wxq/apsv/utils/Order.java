package com.wxq.apsv.utils;

import com.wxq.apsv.model.ApsvOrder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.Charset;

/**
 * 根据指定secret生成订单数据的签名
 */
public final class Order {
    public static String Sign(ApsvOrder order, String secret) {
        String[] data = new String[]{order.time, order.tradeNo, Float.toString(order.amount), order.status, secret};
        String dataStr = String.join("|", data);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(dataStr.getBytes(Charset.forName("UTF-8")));
            byte[] dataBytes = md5.digest();
            StringBuffer buffer = new StringBuffer();
            for (int i=0; i<dataBytes.length; i++) {
                if (Integer.toHexString(0xFF & dataBytes[i]).length() == 1)
                    buffer.append("0").append(
                            Integer.toHexString(0xFF & dataBytes[i]));
                else
                    buffer.append(Integer.toHexString(0xFF & dataBytes[i]));
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}
