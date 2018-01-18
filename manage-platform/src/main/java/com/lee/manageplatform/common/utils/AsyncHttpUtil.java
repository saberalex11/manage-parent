package com.lee.manageplatform.common.utils;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 异步调用的工具类，不能用static，否则会不生效
 */
@Component
public class AsyncHttpUtil {
    private static Logger logger = Logger.getLogger(AsyncHttpUtil.class);
    @Async
    public void sendHttp(String requestUrl){
        logger.info("http发送flume埋点请求，请求为="+requestUrl);
        try {
            HttpGet httppost=new HttpGet(requestUrl);
            httppost.setHeader("Content-Type", "application/json;charset=UTF-8");
            HttpClient hc = new DefaultHttpClient();
            int st = hc.execute(httppost).getStatusLine().getStatusCode();
            logger.info("back status:"+st);
        } catch (ClientProtocolException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
