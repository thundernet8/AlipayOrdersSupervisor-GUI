package com.wxq.apsv.utils;

import com.wxq.apsv.model.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.NameValuePair;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public final class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static HttpClient client = GetClient(); // HttpClientBuilder.create().build();

    private static HttpClient GetClient() {
        if (client != null) {
            return client;
        }
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
                    sslsf).build();
            return httpclient;
        } catch (NoSuchAlgorithmException e) {

        } catch (KeyStoreException e) {

        } catch (KeyManagementException e) {

        }
        return null;
    }

    public static String DoGet(String url, String cookie) {
        // TODO add timeout
        logger.info("DoGet with url: {}, cookie: {}", url, cookie);
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", Constants.DEFAULT_USER_AGENT);
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

    public static String DoPost(String url, String cookie, HashMap<String, Object> data) {
        logger.info("DoPost with url: {}, cookie: {}, data: {}", url, cookie, data.toString());
        HttpPost request = new HttpPost(url);
        request.addHeader("User-Agent", Constants.DEFAULT_USER_AGENT);
        request.addHeader("Cookie", cookie);

        List<NameValuePair> urlParameters = new ArrayList<>();
        Iterator iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            urlParameters.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
        }

        try {
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            logger.info("Response Code : {}", code);
            if (code != 200) {
                return "";
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            logger.error(e.toString());
        }

        return "";
    }
}
