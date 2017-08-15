package com.wxq.apsv.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static HttpClient client = HttpClientBuilder.create().build();

    private static String defaultUA = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";

    public static String DoGet(String url, String cookie) {
        // TODO add timeout
        logger.info("DoGet with url: {}, cookie: {}", url, cookie);
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", defaultUA);
        request.addHeader("Cookie", cookie);

        try {
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            logger.info("Response Code : {}", code);
            if (code != 200) {
                return "";
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "GB18030"));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            // String r1 = new String(result.toString().getBytes("GB18030"),"UTF-8");

            return result.toString();
        } catch (IOException e) {
            logger.error(e.toString());
        }

        return "";
    }
}
