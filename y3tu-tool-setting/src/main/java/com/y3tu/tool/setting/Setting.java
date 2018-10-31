package com.y3tu.tool.setting;

import com.y3tu.tool.core.collection.ListUtil;
import com.y3tu.tool.core.convert.ConvertUtil;
import com.y3tu.tool.core.convert.impl.CharsetConverter;
import com.y3tu.tool.core.io.IOUtil;
import com.y3tu.tool.core.io.resource.ResourceUtil;
import com.y3tu.tool.core.io.watch.SimpleWatcher;
import com.y3tu.tool.core.io.watch.WatchMonitor;
import com.y3tu.tool.core.lang.Assert;
import com.y3tu.tool.core.reflect.ReflectionUtil;
import com.y3tu.tool.core.text.CharsetUtil;
import com.y3tu.tool.core.text.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Looly
 */
@Slf4j
public class Setting extends LinkedHashMap<String, String> {

    /**
     * 数组类型值默认分隔符
     */
    public final static String DEFAULT_DELIMITER = ",";
    /**
     * 默认分组
     */
    public final static String DEFAULT_GROUP = StringUtils.EMPTY;
    /**
     * 默认字符集
     */
    public final static Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;

    /**
     * 附带分组的键值对存储
     */
    private final GroupedMap groupedMap = new GroupedMap();

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
        if (null == this.settingLoader) {
            settingLoader = new SettingLoader(this.groupedMap, this.charset, this.isUseVariable);
        }
        return settingLoader.load(settingUrl);
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
                watchMonitor = WatchMonitor.create(this.settingUrl, StandardWatchEventKinds.ENTRY_MODIFY);
                watchMonitor.setWatcher(new SimpleWatcher() {
                    @Override
                    public void onModify(WatchEvent<?> event, Path currentPath) {
                        load();
                    }
                }).start();
            } catch (Exception e) {
                throw new SettingRuntimeException(e, "Setting auto load not support url: [{}]", this.settingUrl);
            }
            log.debug("Auto load for [{}] listenning...", this.settingUrl);
        } else {
            IOUtil.close(this.watchMonitor);
            this.watchMonitor = null;
        }
    }

    /**
     * @return 获得设定文件的路径
     */
    public String getSettingPath() {
        return (null == this.settingUrl) ? null : this.settingUrl.getPath();
    }

    /**
     * 键值总数
     *
     * @return 键值总数
     */
    @Override
    public int size() {
        return this.groupedMap.size();
    }

    /**
     * 设置值
     *
     * @param key   键
     * @param value 值
     * @return this
     */
    public Setting set(String key, String value) {
        this.groupedMap.put(DEFAULT_GROUP, key, value);
        return this;
    }

    /**
     * 将键值对加入到对应分组中
     *
     * @param group 分组
     * @param key   键
     * @param value 值
     * @return 此key之前存在的值，如果没有返回null
     */
    public String put(String group, String key, String value) {
        return this.groupedMap.put(group, key, value);
    }

    /**
     * 加入多个键值对到某个分组下
     *
     * @param group 分组
     * @param m     键值对
     * @return this
     */
    public Setting putAll(String group, Map<? extends String, ? extends String> m) {
        this.groupedMap.putAll(group, m);
        return this;
    }

    /**
     * 获取并删除键值对，当指定键对应值非空时，返回并删除这个值，后边的键对应的值不再查找
     *
     * @param keys 键列表，常用于别名
     * @return 值
     */
    public Object getAndRemove(String... keys) {
        Object value = null;
        for (String key : keys) {
            value = remove(key);
            if (null != value) {
                break;
            }
        }
        return value;
    }

    /**
     * 获取并删除键值对，当指定键对应值非空时，返回并删除这个值，后边的键对应的值不再查找
     *
     * @param keys 键列表，常用于别名
     * @return 字符串值
     */
    public String getAndRemoveStr(String... keys) {
        Object value = null;
        for (String key : keys) {
            value = remove(key);
            if (null != value) {
                break;
            }
        }
        return (String) value;
    }

    /**
     * 获取配置的值
     *
     * @param key   键
     * @param group 组
     * @return
     */
    public String getByGroup(String key, String group) {
        return this.groupedMap.get(group, key);
    }

    /**
     * 获得指定分组的所有键值对，此方法获取的是原始键值对，获取的键值对可以被修改
     *
     * @param group 分组
     * @return map
     */
    public Map<String, String> getMap(String group) {
        return this.groupedMap.get(group);
    }

    /**
     * 获得group对应的子Setting
     *
     * @param group 分组
     * @return {@link Setting}
     */
    public Setting getSetting(String group) {
        final Setting setting = new Setting();
        group = StringUtils.nullToEmpty(group).trim();
        Map map = this.getMap(group);
        if (map != null) {
            setting.putAll(map);
        }
        return setting;
    }

    /**
     * 转换为Properties对象，原分组变为前缀
     *
     * @param group 分组
     * @return Properties对象
     */
    public Properties getProperties(String group) {
        final Properties properties = new Properties();
        properties.putAll(getMap(group));
        return properties;
    }

    /**
     * 持久化当前设置，会覆盖掉之前的设置<br>
     * 持久化不会保留之前的分组
     *
     * @param absolutePath 设置文件的绝对路径
     */
    public void store(String absolutePath) {
        if (null == this.settingLoader) {
            settingLoader = new SettingLoader(this.groupedMap, this.charset, this.isUseVariable);
        }
        settingLoader.store(absolutePath);
    }


    /**
     * 获取GroupedMap
     *
     * @return GroupedMap
     */
    public GroupedMap getGroupedMap() {
        return this.groupedMap;
    }

    /**
     * 获取所有分组
     *
     * @return 获得所有分组名
     */
    public List<String> getGroups() {
        return ListUtil.newArrayList(this.groupedMap.keySet());
    }

    /**
     * 设置变量的正则<br>
     * 正则只能有一个group表示变量本身，剩余为字符 例如 \$\{(name)\}表示${name}变量名为name的一个变量表示
     *
     * @param regex 正则
     */
    public void setVarRegex(String regex) {
        if (null == this.settingLoader) {
            throw new NullPointerException("SettingLoader is null !");
        }
        this.settingLoader.setVarRegex(regex);
    }

    /**
     * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br>
     * 复杂类型需要用到ConvertUtil工具转换
     *
     * @param bean  Bean 目标对象
     * @param group 组
     * @param clazz 被映射对象的class
     * @return Bean
     */
    public void toBean(Object bean, String group, Class clazz) {
        try {
            Map<String, String> map = this.getSetting(group);
            for (String key : map.keySet()) {
                if (ReflectionUtil.getField(clazz, key) != null) {
                    Class typeClass = ReflectionUtil.getField(clazz, key).getType();

                    if ("Charset".equals(typeClass.getSimpleName())) {
                        ConvertUtil.register(new CharsetConverter(), typeClass);
                    }
                    ReflectionUtil.setFieldValue(bean, key, ConvertUtil.convert(map.get(key), typeClass));
                }
            }
            ConvertUtil.deregister();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数字型型属性值
     *
     * @param key          属性名
     * @param group        分组名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public Integer getInt(String key, String group, Integer defaultValue) {
        return ConvertUtil.toInt(getByGroup(key, group), defaultValue);
    }

    /**
     * 获取数字型型属性值
     *
     * @param key   属性名
     * @param group 分组名
     * @return 属性值
     */
    public Integer getInt(String key, String group) {
        return getInt(key, group, null);
    }

    public String getStr(String key) {
        return getStr(key, DEFAULT_GROUP, "");
    }

    public String getStr(String key, String defaultValue) {
        return getStr(key, DEFAULT_GROUP, defaultValue);
    }

    /**
     * 获得字符串类型值
     *
     * @param key          KEY
     * @param group        分组
     * @param defaultValue 默认值
     * @return 值或默认值
     */
    public String getStr(String key, String group, String defaultValue) {
        final String value = getByGroup(key, group);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取波尔型属性值
     *
     * @param key 属性名
     * @return 属性值
     */
    public Boolean getBool(String key) {
        return getBool(key, DEFAULT_GROUP, null);
    }

    /**
     * 获取波尔型属性值
     *
     * @param key   属性名
     * @param group 分组名
     * @return 属性值
     */
    public Boolean getBool(String key, String group) {
        return getBool(key, group, null);
    }

    /**
     * 获取波尔型型属性值
     *
     * @param key          属性名
     * @param group        分组名
     * @param defaultValue 默认值
     * @return 属性值
     */
    public Boolean getBool(String key, String group, Boolean defaultValue) {
        return ConvertUtil.toBool(getByGroup(key, group), defaultValue);
    }


}
