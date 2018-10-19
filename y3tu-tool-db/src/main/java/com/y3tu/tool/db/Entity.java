package com.y3tu.tool.db;

import com.y3tu.tool.core.bean.BeanUtil;
import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.collection.SetUtil;
import com.y3tu.tool.core.reflect.ReflectionUtil;
import com.y3tu.tool.core.text.CharsetUtil;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.db.sql.SqlUtil;

import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.RowId;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;


/**
 * 数据实体对象<br>
 * 数据实体类充当两个角色：<br>
 * 1. 数据的载体，一个Entity对应数据库中的一个row<br>
 * 2. SQL条件，Entity中的每一个字段对应一个条件，字段值对应条件的值
 *
 * @author loolly
 */
public class Entity extends LinkedHashMap<String, Object> {

    // --------------------------------------------------------------- Static method start

    /**
     * 创建Entity
     *
     * @return Entity
     */
    public static Entity create() {
        return new Entity();
    }

    /**
     * 创建Entity
     *
     * @param tableName 表名
     * @return Entity
     */
    public static Entity create(String tableName) {
        return new Entity(tableName);
    }

    /**
     * 将PO对象转为Entity
     *
     * @param <T>  Bean对象类型
     * @param bean Bean对象
     * @return Entity
     */
    public static <T> Entity parse(T bean) {
        return create(null).parseBean(bean);
    }

    /**
     * 将PO对象转为Entity
     *
     * @param <T>               Bean对象类型
     * @param bean              Bean对象
     * @param isToUnderlineCase 是否转换为下划线模式
     * @param ignoreNullValue   是否忽略值为空的字段
     * @return Entity
     */
    public static <T> Entity parse(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
        return create(null).parseBean(bean, isToUnderlineCase, ignoreNullValue);
    }

    /**
     * 将PO对象转为Entity,并采用下划线法转换字段
     *
     * @param <T>  Bean对象类型
     * @param bean Bean对象
     * @return Entity
     */
    public static <T> Entity parseWithUnderlineCase(T bean) {
        return create(null).parseBean(bean, true, true);
    }
    // --------------------------------------------------------------- Static method end

    /* 表名 */
    private String tableName;
    /* 字段名列表，用于限制加入的字段的值 */
    private Set<String> fieldNames;

    // --------------------------------------------------------------- Constructor start
    public Entity() {
        super();
    }

    /**
     * 构造
     *
     * @param tableName 数据表名
     */

    public Entity(String tableName) {
        super();
        this.tableName = tableName;
    }
    // --------------------------------------------------------------- Constructor end

    // --------------------------------------------------------------- Getters and Setters start

    /**
     * @return 获得表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置表名
     *
     * @param tableName 表名
     * @return 本身
     */
    public Entity setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * @return 字段集合
     */
    public Set<String> getFieldNames() {
        return this.fieldNames;
    }

    /**
     * 设置字段列表，用于限制加入的字段的值
     *
     * @param fieldNames 字段列表
     * @return 自身
     */
    public Entity setFieldNames(Collection<String> fieldNames) {
        if (CollectionUtil.isNotEmpty(fieldNames)) {
            this.fieldNames = new HashSet<String>(fieldNames);
        }
        return this;
    }

    /**
     * 设置字段列表，用于限制加入的字段的值
     *
     * @param fieldNames 字段列表
     * @return 自身
     */
    public Entity setFieldNames(String... fieldNames) {
        if (ArrayUtil.isNotEmpty(fieldNames)) {
            this.fieldNames = SetUtil.newHashSet(fieldNames);
        }
        return this;
    }

    /**
     * 添加字段列表
     *
     * @param fieldNames 字段列表
     * @return 自身
     */
    public Entity addFieldNames(String... fieldNames) {
        if (ArrayUtil.isNotEmpty(fieldNames)) {
            if (null == this.fieldNames) {
                return setFieldNames(fieldNames);
            } else {
                for (String fieldName : fieldNames) {
                    this.fieldNames.add(fieldName);
                }
            }
        }
        return this;
    }

    // --------------------------------------------------------------- Getters and Setters end

    /**
     * 将值对象转换为Entity<br>
     * 类名会被当作表名，小写第一个字母
     *
     * @param <T>  Bean对象类型
     * @param bean Bean对象
     * @return 自己
     */
    public <T> Entity parseBean(T bean) {
        if (StringUtils.isBlank(this.tableName)) {
            this.setTableName(StringUtils.lowerFirst(bean.getClass().getSimpleName()));
        }
        return (Entity) BeanUtil.beanToMap(bean);
    }

    /**
     * 将值对象转换为Entity<br>
     * 类名会被当作表名，小写第一个字母
     *
     * @param <T>               Bean对象类型
     * @param bean              Bean对象
     * @param isToUnderlineCase 是否转换为下划线模式
     * @param ignoreNullValue   是否忽略值为空的字段
     * @return 自己
     */
    public <T> Entity parseBean(T bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
        if (StringUtils.isBlank(this.tableName)) {
            String simpleName = bean.getClass().getSimpleName();
            this.setTableName(isToUnderlineCase ? StringUtils.toUnderlineCase(simpleName) : StringUtils.lowerFirst(simpleName));
        }
        return (Entity) BeanUtil.beanToMap(bean, isToUnderlineCase, ignoreNullValue);
    }

    /**
     * 转换为Bean对象
     * 填充Value Object对象，忽略大小写
     *
     * @param <T>  Bean类型
     * @param bean Bean
     * @return Bean
     */
    public <T> T toBeanIgnoreCase(T bean) {
        return (T) BeanUtil.mapToBean(this, bean.getClass(), true);
    }

    /**
     * 过滤Map保留指定键值对，如果键不存在跳过
     *
     * @param keys 键列表
     * @return Dict 结果
     */
    public Entity filter(String... keys) {
        final Entity result = new Entity(this.tableName);
        result.setFieldNames(this.fieldNames);

        for (String key : keys) {
            if (this.containsKey(key)) {
                result.put(key, this.get(key));
            }
        }
        return result;
    }

    // -------------------------------------------------------------------- Put and Set start
    public Entity set(String field, Object value) {
        this.put(field, value);
        return this;
    }

    public Entity setIgnoreNull(String field, Object value) {
        if (null != field && null != value) {
            set(field, value);
        }
        return this;
    }
    // -------------------------------------------------------------------- Put and Set end

    // -------------------------------------------------------------------- Get start

    /**
     * 获得Clob类型结果
     *
     * @param field 参数
     * @return Clob
     */
    public Clob getClob(String field) {
        return (Clob) this.get(field);
    }

    /**
     * 获得Blob类型结果
     *
     * @param field 参数
     * @return Blob
     */
    public Blob getBlob(String field) {
        return (Blob) this.get(field);
    }

    public Time getTime(String field) {
        Object obj = get(field);
        Time result = null;
        if (null != obj) {
            try {
                result = (Time) obj;
            } catch (Exception e) {
                // try oracle.sql.TIMESTAMP
                result = ReflectionUtil.invoke(obj, "timeValue");
            }
        }
        return result;
    }

    public Date getDate(String field) {
        Object obj = get(field);
        Date result = null;
        if (null != obj) {
            try {
                result = (Date) obj;
            } catch (Exception e) {
                // try oracle.sql.TIMESTAMP
                result = ReflectionUtil.invoke(obj, "dateValue");
            }
        }
        return result;
    }

    public Timestamp getTimestamp(String field) {
        Object obj = get(field);
        Timestamp result = null;
        if (null != obj) {
            try {
                result = (Timestamp) obj;
            } catch (Exception e) {
                // try oracle.sql.TIMESTAMP
                result = ReflectionUtil.invoke(obj, "timestampValue");
            }
        }
        return result;
    }

    public String getStr(String field) {
        return getStr(field, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 获得字符串值<br>
     * 支持Clob、Blob、RowId
     *
     * @param field   字段名
     * @param charset 编码
     * @return 字段对应值
     */
    public String getStr(String field, Charset charset) {
        final Object obj = get(field);
        if (obj instanceof Clob) {
            return SqlUtil.clobToStr((Clob) obj);
        } else if (obj instanceof Blob) {
            return SqlUtil.blobToStr((Blob) obj, charset);
        } else if (obj instanceof RowId) {
            final RowId rowId = (RowId) obj;
            return StringUtils.str(rowId.getBytes(), charset);
        }
        return field;
    }

    /**
     * 获得rowid
     *
     * @return RowId
     */
    public RowId getRowId() {
        return getRowId("ROWID");
    }

    /**
     * 获得rowid
     *
     * @param field rowid属性名
     * @return RowId
     */
    public RowId getRowId(String field) {
        Object obj = this.get(field);
        if (null == obj) {
            return null;
        }
        if (obj instanceof RowId) {
            return (RowId) obj;
        }
        throw new DbRuntimeException("Value of field [{}] is not a rowid!", field);
    }

    // -------------------------------------------------------------------- Get end

    // -------------------------------------------------------------------- 特殊方法 start
    @Override
    public Entity clone() {
        return (Entity) super.clone();
    }
    // -------------------------------------------------------------------- 特殊方法 end

    @Override
    public String toString() {
        return "Entity {tableName=" + tableName + ", fieldNames=" + fieldNames + ", fields=" + super.toString() + "}";
    }
}
