package com.y3tu.tool.setting;

import com.y3tu.tool.core.io.resource.ResourceUtil;
import com.y3tu.tool.core.io.watch.WatchMonitor;
import com.y3tu.tool.core.lang.Assert;
import com.y3tu.tool.core.text.CharsetUtil;

import java.net.URL;
import java.nio.charset.Charset;

public class Setting {


    /**
     * 默认字符集
     */
    public final static Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;
    /**
     * 本设置对象的字符集
     */
    protected Charset charset;
    /**
     * 是否使用变量
     */
    protected boolean isUseVariable;
    /**
     * 设定文件的URL
     */
    protected URL settingUrl;

    private SettingLoader settingLoader;
    private WatchMonitor watchMonitor;


    /**
     * 空构造
     */
    public Setting() {
    }

    /**
     * 构造
     *
     * @param path 相对路径或绝对路径
     */
    public Setting(String path) {
        this(path, false);
    }

    /**
     * 构造
     *
     * @param path          相对路径或绝对路径
     * @param isUseVariable 是否使用变量
     */
    public Setting(String path, boolean isUseVariable) {
        this(path, DEFAULT_CHARSET, isUseVariable);
    }

    /**
     * 构造，使用相对于Class文件根目录的相对路径
     *
     * @param path          相对路径或绝对路径
     * @param charset       字符集
     * @param isUseVariable 是否使用变量
     */
    public Setting(String path, Charset charset, boolean isUseVariable) {
        Assert.notBlank(path, "Blank setting path !");
        this.init(ResourceUtil.asUrl(path), charset, isUseVariable);
    }

    /**
     * 初始化设定文件
     *
     * @param url           {@link URL}
     * @param charset       字符集
     * @param isUseVariable 是否使用变量
     * @return 成功初始化与否
     */
    public boolean init(URL url, Charset charset, boolean isUseVariable) {
        Assert.notNull("url is null");
        this.settingUrl = url;
        this.charset = charset;
        this.isUseVariable = isUseVariable;

        return load();
    }

    /**
     * 重新加载配置文件
     *
     * @return 是否加载成功
     */
    synchronized public boolean load() {
//        if (null == this.settingLoader) {
//            settingLoader = new SettingLoader(this.groupedMap, this.charset, this.isUseVariable);
//        }
//        return settingLoader.load(new UrlResource(this.settingUrl));
        return false;
    }

    /**
     * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br>
     * 只支持基本类型的转换
     *
     * @param bean Bean
     * @return Bean
     */
    public Object toBean(Object bean) {
        return null;
    }


}
