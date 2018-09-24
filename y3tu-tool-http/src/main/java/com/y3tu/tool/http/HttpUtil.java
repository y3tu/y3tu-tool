package com.y3tu.tool.http;

import com.y3tu.tool.http.callback.CallBack;
import okhttp3.Response;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Http请求工具
 * 对okHttp3的封装
 *
 * @author y3tu
 */
public class HttpUtil {
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

}
