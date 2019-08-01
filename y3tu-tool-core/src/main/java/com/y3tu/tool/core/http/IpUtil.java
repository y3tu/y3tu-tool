package com.y3tu.tool.core.http;

import cn.hutool.core.lang.Validator;
import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.ErrorEnum;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.http.pojo.IpLocate;
import com.y3tu.tool.core.http.pojo.Resp;
import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.io.ResourceUtil;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.core.util.ObjectUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.core.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip工具类
 *
 * @author y3tu
 */
@Slf4j
public class IpUtil {

    public static String ipFileTempPath = SystemUtil.get(SystemUtil.TMPDIR) + "ip2region.db";

    static {
        try {
            //第一次加载时清除ip2region.db文件
            File file = FileUtil.file(ipFileTempPath);
            if (file.exists() == true) {
                FileUtil.del(ipFileTempPath);
            }
        } catch (Exception e) {
            throw new ToolException("ip2region.db文件异常", e);
        }
    }

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
     * @return ip地址
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

        if (StrUtil.isNotBlank(ip)) {
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
     * @return ip所在区域
     */
    private static String getIpCity(String ip) {
        if (null != ip) {
            String url = GET_IP_LOCATE + ip;
            String result = "未知";
            try {
                String json = HttpUtil.getSync(url).getData().toString();
                IpLocate locate = JsonUtil.fromJson(json, IpLocate.class);
                if (("200").equals(locate.getRetCode())) {
                    if (StrUtil.isNotBlank(locate.getResult().getProvince())) {
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
     * 获取IP返回地理信息
     * 1.首先使用ip2region工具(https://gitee.com/lionsoul/ip2region)获取ip所在区域
     * 2.如果1的工具获取不到信息，通过网络工具获取区域信息 {@link IpUtil#getIpCity(String)}
     *
     * @param ip ip地址
     * @return ip所在区域
     */
    public static String getCityInfo(String ip) {
        try {
            File file = new File(ipFileTempPath);
            if (file.exists() == false) {
                InputStream inputStream = ResourceUtil.getStream("ip2region/ip2region.db");
                file = FileUtil.writeFromStream(inputStream, file);
            }
            int algorithm = DbSearcher.BTREE_ALGORITHM;
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, file.getPath());
            Method method = null;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
                default:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }
            DataBlock dataBlock = null;
            if (!Validator.isIpv4(ip)) {
                log.error("ErrorEnum: Invalid ip address");
                throw new ToolException("ErrorEnum: Invalid ip address", ErrorEnum.UTIL_EXCEPTION);
            }
            dataBlock = (DataBlock) method.invoke(searcher, ip);
            if (ObjectUtil.isNull(dataBlock)) {
                //如果查询不到就使用网络方式查询
                return getIpCity(ip);
            }
            return dataBlock.getRegion();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取地址信息异常", e);
            throw new ToolException("获取地址信息异常：", e);
        }
    }

    /**
     * 通过http://ip.chinaz.com 获取外网ip地址
     *
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

        final Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
        }
        return ip;
    }

}
