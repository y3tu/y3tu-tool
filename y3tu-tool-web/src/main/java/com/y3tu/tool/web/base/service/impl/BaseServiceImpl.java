package com.y3tu.tool.web.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.lang.Editor;
import com.y3tu.tool.core.util.IdUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.web.base.mapper.BaseMapper;
import com.y3tu.tool.web.base.pojo.PageInfo;
import com.y3tu.tool.web.base.service.BaseService;


import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author y3tu
 * @date 2018/1/29
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public PageInfo<T> queryPage(PageInfo<T> pageInfo, Map<String, Object> map) {
        if (pageInfo != null) {
            //表示分页
            baseMapper.queryPage(pageInfo.getPage(), map);
            pageInfo.setCurrentPage(pageInfo.getPage().getCurrent());
            pageInfo.setList(pageInfo.getPage().getRecords());
            pageInfo.setTotalCount(pageInfo.getPage().getTotal());
            pageInfo.setTotalPage(pageInfo.getPage().getPages());

        } else {
            //不分页，查全量数据
            List<T> list = baseMapper.queryPage(map);
            pageInfo.setTotalCount(list.size());
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    /**
     * 重写插入数据方法，添加注解生成id
     *
     * @param entity
     * @return
     */
    @Override
    public boolean save(T entity) {
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
            throw new IllegalArgumentException("Error: entityList must not be empty");
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
                            int id = (int) IdUtil.createSnowflake(1, 1).nextId();
                            f.set(entity, id);
                        } else if (f.getType() == Long.class) {
                            long id = IdUtil.createSnowflake(1, 1).nextId();
                            f.set(entity, id);
                        } else if (f.getType() == String.class) {
                            long id = IdUtil.createSnowflake(1, 1).nextId();
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
