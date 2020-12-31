package com.y3tu.tool.web.base.jpa;

import java.util.List;
import java.util.Set;

/**
 * 基础服务接口
 *
 * @author y3tu
 */
public interface BaseService<T> {

    /**
     * 分页查询
     *
     * @param pageInfo 分页信息 包含每页多少条,当前页数,排序,查询条件
     * @return 分页查询数据
     */
    PageInfo<T> page(PageInfo<T> pageInfo);

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return
     */
    List<T> queryAll(T criteria);

    /**
     * 根据主键查询
     *
     * @param key 主键
     * @return
     */
    Object findById(Object key);

    /**
     * 创建
     *
     * @param entity 实体
     */
    void create(T entity);

    /**
     * 编辑
     *
     * @param
     */
    void update(T entity);

    /**
     * 删除
     *
     * @param
     */
    void delete(Object[] keys);


    /**
     * 删除
     *
     * @param
     */
    void delete(Object key);
}
