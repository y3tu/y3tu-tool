package com.y3tu.tool.http;

import com.alibaba.fastjson.JSONObject;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.http.pojo.IpLocate;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class IpUtil {
    /**
     * http://www.mob.com/
     * 你的APPKEY mob官网注册申请即可
     */
    private final static String APPKEY = "2739954e223d8";

    /**
     * Mob IP查询接口
     */
    private final static String GET_IP_LOCATE = "http://apicloud.mob.com/ip/query?key=" + APPKEY + "&ip=";

    /**
     * Mob IP天气查询接口
     */
    private final static String GET_IP_WEATHER = "http://apicloud.mob.com/v1/weather/ip?key=" + APPKEY + "&ip=";

    /**
     * 获取客户端IP地址
     *
     * @param request 请求
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 获取IP返回地理天气信息
     *
     * @param ip ip地址
     * @return
     */
    public static String getIpWeatherInfo(String ip) {

        if (StringUtils.isNotBlank(ip)) {
            String url = GET_IP_WEATHER + ip;
            Resp result = HttpUtil.getSync(url);
            return result.getData().toString();
        }
        return null;
    }

    /**
     * 获取IP返回地理信息
     *
     * @param ip ip地址
     * @return
     */
    public static String getIpCity(String ip) {
        if (null != ip) {
            String url = GET_IP_LOCATE + ip;
            String result = "未知";
            try {
                String json = HttpUtil.getSync(url).getData().toString();
                IpLocate locate = JSONObject.parseObject(json, IpLocate.class);
                if (("200").equals(locate.getRetCode())) {
                    if (StringUtils.isNotBlank(locate.getResult().getProvince())) {
                        result = locate.getResult().getProvince() + " " + locate.getResult().getCity();
                    } else {
                        result = locate.getResult().getCountry();
                    }
                }
            } catch (Exception e) {
                log.info("获取IP信息失败");
            }
            return result;
        }
        return null;
    }

    /**
     * 通过http://ip.chinaz.com 获取外网ip地址
     * @return
     */
    public static String getOutsideIp() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";

        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        final Pattern p= Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
        }
        return ip;
    }

}
