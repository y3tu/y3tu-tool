package com.y3tu.tool.web.base.mybatis;

import cn.hutool.core.lang.Editor;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.util.IdUtil;
import com.y3tu.tool.core.util.ObjectUtil;
import com.y3tu.tool.core.util.StrUtil;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基础service实现
 *
 * @author y3tu
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {
    /**
     * 终端ID 生成唯一主键id时，雪花算法使用
     */
    int workerId = 1;
    /**
     * 数据中心id 生成唯一主键id时，雪花算法使用
     */
    int dataCenterId = 1;

    @Override
    public PageInfo<T> page(PageInfo<T> pageInfo) {
        //每次查询前把total置为0
        pageInfo.setTotal(0);
        return baseMapper.page(pageInfo);
    }

    @Override
    public PageInfo<T> page(PageInfo<T> pageInfo, Wrapper<T> wrapper) {
        //每次查询前把total置为0
        pageInfo.setTotal(0);
        return (PageInfo<T>) baseMapper.selectPage(pageInfo, wrapper);
    }

    @Override
    public List<T> noPage(Map params) {
        return baseMapper.page(params);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean saveBySnowflakeId(T entity) {
        return retBool(baseMapper.insert(fillEntityId(entity, workerId, dataCenterId)));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean saveBySnowflakeId(T entity, int workerId, int dataCenterId) {
        return retBool(baseMapper.insert(fillEntityId(entity, workerId, dataCenterId)));
    }


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean saveBatchBySnowflakeId(Collection<T> entityList, int batchSize) {
        return this.saveBatchBySnowflakeId(entityList, batchSize, workerId, dataCenterId);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean saveBatchBySnowflakeId(Collection<T> entityList, int batchSize, int workerId, int dataCenterId) {
        if (CollectionUtil.isEmpty(entityList)) {
            throw new IllegalArgumentException("ErrorEnum: entityList must not be empty");
        }
        CollectionUtil.filter(entityList, new Editor<T>() {
            @Override
            public T edit(T t) {
                return fillEntityId(t, workerId, dataCenterId);
            }
        });
        return super.saveBatch(entityList, batchSize);
    }

    /**
     * 填充主键
     *
     * @param entity
     */
    private T fillEntityId(T entity, int workerId, int dataCenterId) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StrUtil.isNotEmpty(tableInfo.getKeyProperty())) {
                try {
                    Field f = cls.getDeclaredField(tableInfo.getKeyProperty());
                    f.setAccessible(true);
                    if (ObjectUtil.isEmpty(f.get(entity))) {
                        if (f.getType() == Integer.class) {
                            int id = (int) IdUtil.createSnowflake(workerId, dataCenterId).nextId();
                            f.set(entity, id);
                        } else if (f.getType() == Long.class) {
                            long id = IdUtil.createSnowflake(workerId, dataCenterId).nextId();
                            f.set(entity, id);
                        } else if (f.getType() == String.class) {
                            long id = IdUtil.createSnowflake(workerId, dataCenterId).nextId();
                            f.set(entity, String.valueOf(id));
                        } else {
                            throw new ToolException("主键类型不可用：" + f.getType().getName());
                        }
                    }
                } catch (NoSuchFieldException e) {
                    throw new ToolException("没有主键：" + tableInfo.getKeyProperty());
                } catch (IllegalAccessException e) {
                    throw new ToolException("主键不可访问：" + tableInfo.getKeyProperty());
                }
            }
        }
        return entity;
    }
}
