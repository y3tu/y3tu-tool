package com.y3tu.tool.core.http;

import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.collection.IterUtil;
import com.y3tu.tool.core.http.callback.CallBack;
import com.y3tu.tool.core.http.pojo.Resp;
import com.y3tu.tool.core.map.MapUtil;
import com.y3tu.tool.core.util.CharsetUtil;
import com.y3tu.tool.core.util.StrUtil;
import okhttp3.Response;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Http请求工具
 * 对okHttp3的封装
 *
 * @author y3tu
 * @see cn.hutool.http.HttpUtil
 */
public class HttpUtil extends cn.hutool.http.HttpUtil {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    public static final String FILE_TYPE_FILE = "file/*";
    public static final String FILE_TYPE_IMAGE = "image/*";
    public static final String FILE_TYPE_AUDIO = "audio/*";
    public static final String FILE_TYPE_VIDEO = "video/*";

    /**
     * get请求 (异步)
     *
     * @param url      请求url
     * @param callBack 回调接口 onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void get(String url, CallBack callBack) {
        get(url, null, callBack);
    }

    /**
     * get请求(异步)，可以传递参数
     *
     * @param url：url
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void get(String url, Map<String, String> paramsMap, CallBack callBack) {
        http(METHOD_GET, url, paramsMap, null, callBack);
    }


    /**
     * get请求 (同步)
     *
     * @param url 请求url
     */
    public static Resp getSync(String url) {
        return getSync(url, null);
    }

    /**
     * get请求(同步)，可以传递参数
     *
     * @param url：url
     * @param paramsMap：map集合，封装键值对参数
     */
    public static Resp getSync(String url, Map<String, String> paramsMap) {
        return httpSync(METHOD_GET, url, paramsMap, null);
    }

    /**
     * post请求 (异步)
     *
     * @param url      请求url
     * @param callBack 回调接口 onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void post(String url, CallBack callBack) {
        post(url, null, callBack);
    }

    /**
     * post请求(异步)，可以传递参数
     *
     * @param url：url
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void post(String url, Map<String, String> paramsMap, CallBack callBack) {
        http(METHOD_POST, url, paramsMap, null, callBack);
    }


    /**
     * post请求 (同步)
     *
     * @param url 请求url
     */
    public static Resp postSync(String url) {
        return postSync(url, null);
    }

    /**
     * post请求(同步)，可以传递参数
     *
     * @param url：url
     * @param paramsMap：map集合，封装键值对参数
     */
    public static Resp postSync(String url, Map<String, String> paramsMap) {
        return httpSync(METHOD_POST, url, paramsMap, null);
    }


    /**
     * 请求(同步)，可以传递参数
     *
     * @param methodType               请求方法
     * @param url：url
     * @param paramsMap：map集合，封装键值对参数
     * @param headerMap：map集合，封装请求头键值对
     */
    public static Resp httpSync(String methodType, String url, Map<String, String> paramsMap, Map<String, String> headerMap) {
        try {
            Response response = new OkHttpUtil(methodType, url, paramsMap, headerMap, null).executeSync();
            return new Resp(response.code(), response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
            throw new HttpException("Http请求异常", e);
        }
    }

    /**
     * 请求(异步)，可以传递参数
     *
     * @param url：url
     * @param paramsMap：map集合，封装键值对参数
     * @param headerMap：map集合，封装请求头键值对
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void http(String methodType, String url, Map<String, String> paramsMap, Map<String, String> headerMap, CallBack callBack) {
        new OkHttpUtil(methodType, url, paramsMap, headerMap, callBack).execute();
    }


    /**
     * postJson请求(异步)，可以传递参数
     *
     * @param url：url
     * @param jsonStr：json格式的键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void postJson(String url, String jsonStr, CallBack callBack) {
        postJson(url, jsonStr, null, callBack);
    }

    /**
     * postJson请求(异步)，可以传递参数
     *
     * @param url：url
     * @param jsonStr：json格式的键值对参数
     * @param headerMap：map集合，封装请求头键值对
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void postJson(String url, String jsonStr, Map<String, String> headerMap, CallBack callBack) {
        new OkHttpUtil(METHOD_POST, url, jsonStr, headerMap, callBack).execute();
    }


    /**
     * postJson请求(同步)，可以传递参数
     *
     * @param url：url
     * @param jsonStr：json格式的键值对参数
     */
    public static void postJsonSync(String url, String jsonStr) {
        postJson(url, jsonStr, null);
    }

    /**
     * postJson请求(同步)，可以传递参数
     *
     * @param url：url
     * @param jsonStr：json格式的键值对参数
     * @param headerMap：map集合，封装请求头键值对
     */
    public static void postJsonSync(String url, String jsonStr, Map<String, String> headerMap) {
        new OkHttpUtil(METHOD_POST, url, jsonStr, headerMap, null).executeSync();
    }


    /**
     * post请求，上传单个文件
     *
     * @param url：url
     * @param file：File对象
     * @param fileKey：上传参数时file对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，还可以重写onProgress方法，得到上传进度
     */
    public static void uploadFile(String url, File file, String fileKey, String fileType, CallBack callBack) {
        uploadFile(url, file, fileKey, fileType, null, callBack);
    }

    /**
     * post请求，上传单个文件
     *
     * @param url：url
     * @param file：File对象
     * @param fileKey：上传参数时file对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，还可以重写onProgress方法，得到上传进度
     */
    public static void uploadFile(String url, File file, String fileKey, String fileType, Map<String, String> paramsMap, CallBack callBack) {
        uploadFile(url, file, fileKey, fileType, paramsMap, null, callBack);
    }

    /**
     * post请求，上传单个文件
     *
     * @param url：url
     * @param file：File对象
     * @param fileKey：上传参数时file对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param headerMap：map集合，封装请求头键值对
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，还可以重写onProgress方法，得到上传进度
     */
    public static void uploadFile(String url, File file, String fileKey, String fileType, Map<String, String> paramsMap, Map<String, String> headerMap, CallBack callBack) {
        new OkHttpUtil(METHOD_POST, url, paramsMap, file, fileKey, fileType, headerMap, callBack).execute();
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     *
     * @param url：url
     * @param fileList：集合元素是File对象
     * @param fileKey：上传参数时fileList对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void uploadListFile(String url, List<File> fileList, String fileKey, String fileType, CallBack callBack) {
        uploadListFile(url, null, fileList, fileKey, fileType, callBack);
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     *
     * @param url：url
     * @param fileList：集合元素是File对象
     * @param fileKey：上传参数时fileList对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void uploadListFile(String url, Map<String, String> paramsMap, List<File> fileList, String fileKey, String fileType, CallBack callBack) {
        uploadListFile(url, paramsMap, fileList, fileKey, fileType, null, callBack);
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     *
     * @param url：url
     * @param fileList：集合元素是File对象
     * @param fileKey：上传参数时fileList对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param headerMap：map集合，封装请求头键值对
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void uploadListFile(String url, Map<String, String> paramsMap, List<File> fileList, String fileKey, String fileType, Map<String, String> headerMap, CallBack callBack) {
        new OkHttpUtil(METHOD_POST, url, paramsMap, fileList, fileKey, fileType, headerMap, callBack).execute();
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     *
     * @param url：url
     * @param fileMap：集合key是File对象对应的键，集合value是File对象
     * @param fileType：File类型，是image，video，audio，file
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void uploadMapFile(String url, Map<String, File> fileMap, String fileType, CallBack callBack) {
        uploadMapFile(url, fileMap, fileType, null, callBack);
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     *
     * @param url：url
     * @param fileMap：集合key是File对象对应的键，集合value是File对象
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void uploadMapFile(String url, Map<String, File> fileMap, String fileType, Map<String, String> paramsMap, CallBack callBack) {
        uploadMapFile(url, fileMap, fileType, paramsMap, null, callBack);
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     *
     * @param url：url
     * @param fileMap：集合key是File对象对应的键，集合value是File对象
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param headerMap：map集合，封装请求头键值对
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用
     */
    public static void uploadMapFile(String url, Map<String, File> fileMap, String fileType, Map<String, String> paramsMap, Map<String, String> headerMap, CallBack callBack) {
        new OkHttpUtil(METHOD_POST, url, paramsMap, fileMap, fileType, headerMap, callBack).execute();
    }

    /**
     * 下载文件,不带参数
     */
    public static void downloadFile(String url, CallBack callBack) {
        downloadFile(url, null, callBack);
    }

    /**
     * 下载文件,带参数
     */
    public static void downloadFile(String url, Map<String, String> paramsMap, CallBack callBack) {
        get(url, paramsMap, callBack);
    }

    /**
     * 加载图片
     */
    public static void getBitmap(String url, CallBack callBack) {
        getBitmap(url, null, callBack);
    }

    /**
     * 加载图片，带参数
     */
    public static void getBitmap(String url, Map<String, String> paramsMap, CallBack callBack) {
        get(url, paramsMap, callBack);
    }

    /**
     * 编码字符为 application/x-www-form-urlencoded，使用UTF-8编码
     *
     * @param content 被编码内容
     * @return 编码后的字符
     */
    public static String encodeUtf8(String content) {
        return encode(content, CharsetUtil.UTF_8);
    }

    /**
     * 编码字符为 application/x-www-form-urlencoded
     *
     * @param content 被编码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String encode(String content, Charset charset) {
        if (null == charset) {
            charset = CharsetUtil.defaultCharset();
        }
        return encode(content, charset.name());
    }

    /**
     * 编码字符为 application/x-www-form-urlencoded
     *
     * @param content    被编码内容
     * @param charsetStr 编码
     * @return 编码后的字符
     * @throws HttpException 编码不支持
     */
    public static String encode(String content, String charsetStr) throws HttpException {
        if (StrUtil.isBlank(content)) {
            return content;
        }

        String encodeContent = null;
        try {
            encodeContent = URLEncoder.encode(content, charsetStr);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(StrUtil.format("Unsupported encoding: [{}]", charsetStr), e);
        }
        return encodeContent;
    }

    /**
     * 解码application/x-www-form-urlencoded字符
     *
     * @param content 被解码内容
     * @param charset 编码
     * @return 编码后的字符
     */
    public static String decode(String content, Charset charset) {
        return decode(content, charset.name());
    }

    /**
     * 解码application/x-www-form-urlencoded字符
     *
     * @param content    被解码内容
     * @param charsetStr 编码
     * @return 编码后的字符
     */
    public static String decode(String content, String charsetStr) {
        if (StrUtil.isBlank(content)) {
            return content;
        }
        String encodeContnt = null;
        try {
            encodeContnt = URLDecoder.decode(content, charsetStr);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException(StrUtil.format("Unsupported encoding: [{}]", charsetStr), e);
        }
        return encodeContnt;
    }

    /**
     * 将URL参数解析为Map（也可以解析Post中的键值对参数）
     *
     * @param paramsStr 参数字符串（或者带参数的Path）
     * @param charset   字符集
     * @return 参数Map
     */
    public static HashMap<String, String> decodeParamMap(String paramsStr, String charset) {
        final Map<String, List<String>> paramsMap = decodeParams(paramsStr, charset);
        final HashMap<String, String> result = MapUtil.newHashMap(paramsMap.size());
        List<String> valueList;
        for (Map.Entry<String, List<String>> entry : paramsMap.entrySet()) {
            valueList = entry.getValue();
            result.put(entry.getKey(), CollectionUtil.isEmpty(valueList) ? null : valueList.get(0));
        }
        return result;
    }

    /**
     * 将URL参数解析为Map（也可以解析Post中的键值对参数）
     *
     * @param paramsStr 参数字符串（或者带参数的Path）
     * @param charset   字符集
     * @return 参数Map
     */
    public static Map<String, List<String>> decodeParams(String paramsStr, String charset) {
        if (StrUtil.isBlank(paramsStr)) {
            return Collections.emptyMap();
        }

        // 去掉Path部分
        int pathEndPos = paramsStr.indexOf('?');
        if (pathEndPos > -1) {
            paramsStr = StrUtil.subSuf(paramsStr, pathEndPos + 1);
        }

        final Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
        final int len = paramsStr.length();
        String name = null;
        int pos = 0; // 未处理字符开始位置
        int i; // 未处理字符结束位置
        char c; // 当前字符
        for (i = 0; i < len; i++) {
            c = paramsStr.charAt(i);
            if (c == '=') { // 键值对的分界点
                if (null == name) {
                    // name可以是""
                    name = paramsStr.substring(pos, i);
                }
                pos = i + 1;
            } else if (c == '&') { // 参数对的分界点
                if (null == name && pos != i) {
                    // 对于像&a&这类无参数值的字符串，我们将name为a的值设为""
                    addParam(params, paramsStr.substring(pos, i), StrUtil.EMPTY, charset);
                } else if (name != null) {
                    addParam(params, name, paramsStr.substring(pos, i), charset);
                    name = null;
                }
                pos = i + 1;
            }
        }

        // 处理结尾
        if (pos != i) {
            if (name == null) {
                addParam(params, paramsStr.substring(pos, i), StrUtil.EMPTY, charset);
            } else {
                addParam(params, name, paramsStr.substring(pos, i), charset);
            }
        } else if (name != null) {
            addParam(params, name, StrUtil.EMPTY, charset);
        }

        return params;
    }

    /**
     * 将键值对加入到值为List类型的Map中
     *
     * @param params  参数
     * @param name    key
     * @param value   value
     * @param charset 编码
     */
    private static void addParam(Map<String, List<String>> params, String name, String value, String charset) {
        name = decode(name, charset);
        value = decode(value, charset);
        List<String> values = params.get(name);
        if (values == null) {
            values = new ArrayList<String>(1); // 一般是一个参数
            params.put(name, values);
        }
        values.add(value);
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式，不做编码
     *
     * @param paramMap 表单数据
     * @return url参数
     */
    public static String toParams(Map<String, ?> paramMap) {
        return toParams(paramMap, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式<br>
     * 编码键和值对
     *
     * @param paramMap    表单数据
     * @param charsetName 编码
     * @return url参数
     */
    public static String toParams(Map<String, Object> paramMap, String charsetName) {
        return toParams(paramMap, CharsetUtil.charset(charsetName));
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式<br>
     * paramMap中如果key为空（null和""）会被忽略，如果value为null，会被做为空白符（""）<br>
     * 会自动url编码键和值
     *
     * <pre>
     * key1=v1&amp;key2=&amp;key3=v3
     * </pre>
     *
     * @param paramMap 表单数据
     * @param charset  编码
     * @return url参数
     */
    public static String toParams(Map<String, ?> paramMap, Charset charset) {
        if (MapUtil.isEmpty(paramMap)) {
            return StrUtil.EMPTY;
        }
        if (null == charset) {
            //默认编码为系统编码
            charset = CharsetUtil.CHARSET_UTF_8;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        String key;
        Object value;
        String valueStr;
        for (Map.Entry<String, ?> item : paramMap.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("&");
            }
            key = item.getKey();
            value = item.getValue();
            if (value instanceof Iterable) {
                value = IterUtil.join((Iterable<?>) value, ",");
            } else if (value instanceof Iterator) {
                value = IterUtil.join((Iterator<?>) value, ",");
            }
            valueStr = String.valueOf(value);
            if (StrUtil.isNotEmpty(key)) {
                sb.append(encode(key, charset)).append("=");
                if (StrUtil.isNotEmpty(valueStr)) {
                    sb.append(encode(valueStr, charset));
                }
            }
        }
        return sb.toString();
    }
}
