package com.wxq.apsv.utils;

import com.wxq.apsv.model.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.NameValuePair;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

// 一些注意事项 http://blog.csdn.net/shootyou/article/details/6615051

/**
 * 利用Apache HttpClient 进行GET/POST请求的封装
 */
public final class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static HttpClient client = GetClient();

    private static HttpClient GetClient() {
        if (client != null) {
            return client;
        }
        try {
            // 解决自定义SSL证书以及不受信CA证书链的异常问题(即忽略证书校验)
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            return HttpClients.custom().setSSLSocketFactory(
                    sslsf).build();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.toString());
        } catch (KeyStoreException e) {
            logger.error(e.toString());
        } catch (KeyManagementException e) {
            logger.error(e.toString());
        }
        return null;
    }

    public static String DoGet(String url, String cookie) {
        logger.info("DoGet with url: {}, cookie: {}", url, cookie);
        HttpGet request = new HttpGet(url);
        request.setConfig(RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).setConnectionRequestTimeout(10000).build());
        request.addHeader("User-Agent", Constants.DEFAULT_USER_AGENT);
        request.addHeader("Cookie", cookie);

        InputStream in = null;
        InputStreamReader isr = null;
        try {
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            logger.info("Response Code : {}", code);
            if (code != 200 && code != 201) {
                request.abort();
                return "";
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                in = entity.getContent();
                isr = new InputStreamReader(in, "GB18030");
            }
            BufferedReader rd = new BufferedReader(isr);
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        } catch (IOException e) {
            request.abort();
            logger.error(e.toString());
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            }
        }

        return "";
    }

    public static String DoPost(String url, String cookie, HashMap<String, Object> data) {
        logger.info("DoPost with url: {}, cookie: {}, data: {}", url, cookie, data.toString());
        HttpPost request = new HttpPost(url);
        request.setConfig(RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).setConnectionRequestTimeout(10000).build());
        request.addHeader("User-Agent", Constants.DEFAULT_USER_AGENT);
        request.addHeader("Cookie", cookie);

        List<NameValuePair> urlParameters = new ArrayList<>();
        Iterator iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            urlParameters.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
        }

        InputStream in = null;
        InputStreamReader isr = null;

        try {
            request.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            logger.info("Response Code : {}", code);
            if (code != 200 && code != 201) {
                request.abort();
                return "";
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                in = entity.getContent();
                isr = new InputStreamReader(in, "UTF-8");
            }
            BufferedReader rd = new BufferedReader(isr);
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            request.abort();
            logger.error(e.toString());
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            }
        }

        return "";
    }
}
