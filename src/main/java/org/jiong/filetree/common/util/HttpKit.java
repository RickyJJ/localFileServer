package org.jiong.filetree.common.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jiong.filetree.common.constants.AppConst;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Jiong
 */
@Slf4j
public class HttpKit {
    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        final String UNKNOWN_IP = "unknown";
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || UNKNOWN_IP.equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static Map<String, String> post(String url, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(AppConst.HTTP_TIME_OUT).build();

        RequestBuilder requestBuilder = RequestBuilder.post(url);
        params.forEach(requestBuilder::addParameter);
        requestBuilder.setConfig(requestConfig);

        HttpUriRequest httpUriRequest = requestBuilder.build();
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpUriRequest);

            String result = EntityUtils.toString(httpResponse.getEntity());
            log.debug("http result: {}", result);
            return JSONObject.parseObject(result, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Http error", e);
            return null;
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
    }
}
