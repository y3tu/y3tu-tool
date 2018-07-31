package com.y3tu.tool.setting;

import com.y3tu.tool.core.io.FilePathUtil;
import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.io.IORuntimeException;
import com.y3tu.tool.core.io.IoUtil;
import com.y3tu.tool.core.io.resource.ResourceUtil;
import com.y3tu.tool.core.io.watch.SimpleWatcher;
import com.y3tu.tool.core.io.watch.WatchMonitor;
import com.y3tu.tool.core.lang.Assert;
import com.y3tu.tool.core.text.CharsetUtil;
import com.y3tu.tool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.Properties;

/**
 * Properties文件读取封装类
 */
@Slf4j
public class Props extends Properties {

    // ----------------------------------------------------------------------- 私有属性 start

    /**
     * 属性文件的URL
     */
    private URL propertiesFileUrl;
    private WatchMonitor watchMonitor;
    /**
     * properties文件编码
     */
    private Charset charset = CharsetUtil.CHARSET_ISO_8859_1;
    // ----------------------------------------------------------------------- 私有属性 end

    /**
     * 获得Classpath下的Properties文件
     *
     * @param resource 资源（相对Classpath的路径）
     * @return Properties
     */
    public static Properties getProp(String resource) {
        return new Props(resource);
    }

    /**
     * 获得Classpath下的Properties文件
     *
     * @param resource    资源（相对Classpath的路径）
     * @param charsetName 字符集
     * @return Properties
     */
    public static Properties getProp(String resource, String charsetName) {
        return new Props(resource, charsetName);
    }

    /**
     * 获得Classpath下的Properties文件
     *
     * @param resource 资源（相对Classpath的路径）
     * @param charset  字符集
     * @return Properties
     */
    public static Properties getProp(String resource, Charset charset) {
        return new Props(resource, charset);
    }


    // ----------------------------------------------------------------------- 构造方法 start

    /**
     * 构造
     */
    public Props() {
        super();
    }


    /**
     * 构造，使用相对于Class文件根目录的相对路径
     *
     * @param path
     */
    public Props(String path) {
        this(path, CharsetUtil.ISO_8859_1);
    }

    /**
     * 构造，使用相对于Class文件根目录的相对路径
     *
     * @param path        相对或绝对路径
     * @param charsetName 字符集
     */
    public Props(String path, String charsetName) {
        this(path, CharsetUtil.charset(charsetName));
    }

    /**
     * 构造，使用相对于Class文件根目录的相对路径
     *
     * @param path    相对或绝对路径
     * @param charset 字符集
     */
    public Props(String path, Charset charset) {
        Assert.notBlank(path, "Blank properties file path !");
        if (null != charset) {
            this.charset = charset;
        }
        this.load(ResourceUtil.asUrlByPath(path));
    }

    /**
     * 构造
     *
     * @param propertiesFile 配置文件对象
     */
    public Props(File propertiesFile) {
        this(propertiesFile, StandardCharsets.ISO_8859_1);
    }

    /**
     * 构造
     *
     * @param propertiesFile 配置文件对象
     * @param charsetName    字符集
     */
    public Props(File propertiesFile, String charsetName) {
        this(propertiesFile, Charset.forName(charsetName));
    }

    /**
     * 构造
     *
     * @param propertiesFile 配置文件对象
     * @param charset        字符集
     */
    public Props(File propertiesFile, Charset charset) {
        Assert.notNull(propertiesFile, "Null properties file!");
        this.charset = charset;
        try {
            this.load(propertiesFile.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    /**
     * 初始化配置文件
     *
     * @param url 配置文化url
     */
    public void load(URL url) {
        this.propertiesFileUrl = url;
        if (null == this.propertiesFileUrl) {
            throw new SettingRuntimeException("Can not find properties file: [{}]", propertiesFileUrl);
        }
        log.debug("Load properties [{}]", propertiesFileUrl.getPath());
        try {
            BufferedReader reader = IoUtil.getReader(URLUtil.getReader(this.propertiesFileUrl, charset));
            super.load(reader);
        } catch (Exception e) {
            log.error("Load properties error!", e);
        }
    }

    /**
     * 重新加载配置文件
     */
    public void load() {
        this.load(this.propertiesFileUrl);
    }


    /**
     * 在配置文件变更时自动加载
     *
     * @param autoReload 是否自动加载
     */
    public void autoLoad(boolean autoReload) {
        if (autoReload) {
            if (null != this.watchMonitor) {
                this.watchMonitor.close();
            }
            try {
                watchMonitor = WatchMonitor.create(Paths.get(this.propertiesFileUrl.toURI()));
                watchMonitor.setWatcher(new SimpleWatcher() {
                    @Override
                    public void onModify(WatchEvent<?> event, Path currentPath) {
                        load();
                    }
                }).start();
            } catch (Exception e) {
                throw new SettingRuntimeException(e, "Setting auto load not support url: [{}]", this.propertiesFileUrl);
            }
        } else {
            IoUtil.close(this.watchMonitor);
            this.watchMonitor = null;
        }
    }

    // ----------------------------------------------------------------------- Set start

    /**
     * 设置值，无给定键创建之。设置后未持久化
     *
     * @param key   属性键
     * @param value 属性值
     */
    public void setProperty(String key, Object value) {
        super.setProperty(key, value.toString());
    }

    /**
     * 持久化当前设置，会覆盖掉之前的设置
     *
     * @param absolutePath 设置文件的绝对路径
     * @throws IORuntimeException IO异常，可能为文件未找到
     */
    public void store(String absolutePath) throws IORuntimeException {
        Writer writer = null;
        try {
            writer = FileUtil.asBufferedWriter(absolutePath, charset);
            super.store(writer, null);
        } catch (IOException e) {
            throw new IORuntimeException(e, "Store properties to [{}] error!", absolutePath);
        } finally {
            IoUtil.close(writer);
        }
    }

    /**
     * 存储当前设置，会覆盖掉以前的设置
     *
     * @param path  相对路径
     * @param clazz 相对的类
     */
    public void store(String path, Class<?> clazz) {
        this.store(FilePathUtil.getAbsolutePath(path, clazz));
    }
    // ----------------------------------------------------------------------- Set end

}
