package com.y3tu.tool.web.base.service.impl;

import cn.hutool.core.lang.Editor;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.util.IdUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.web.base.mapper.BaseMapper;
import com.y3tu.tool.web.base.pojo.PageInfo;
import com.y3tu.tool.web.base.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author y3tu
 * @date 2018/1/29
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

    /**
     * 重写插入数据方法，采用雪花算法生成主键id
     *
     * @param entity
     * @return
     */
    @Override
    public boolean save(T entity) {
        return retBool(baseMapper.insert(fillEntityId(entity)));
    }

    /**
     * 重写插入数据方法，采用雪花算法生成主键id
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public boolean save(T entity, int workerId, int dataCenterId) {
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        return retBool(baseMapper.insert(fillEntityId(entity)));
    }


    /**
     * 批量插入
     *
     * @param entityList
     * @param batchSize
     * @return
     */
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        if (CollectionUtil.isEmpty(entityList)) {
            throw new IllegalArgumentException("ErrorEnum: entityList must not be empty");
        }
        CollectionUtil.filter(entityList, new Editor<T>() {
            @Override
            public T edit(T t) {
                return fillEntityId(t);
            }
        });
        return super.saveBatch(entityList, batchSize);
    }

    /**
     * 填充主键
     *
     * @param entity
     */
    private T fillEntityId(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StrUtil.isNotEmpty(tableInfo.getKeyProperty())) {
                try {
                    Field f = cls.getDeclaredField(tableInfo.getKeyProperty());
                    f.setAccessible(true);
                    if (f.get(entity) == null) {
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
