package com.y3tu.tool.web.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.y3tu.tool.web.base.pojo.PageInfo;

import java.util.Collection;

/**
 * 基本Service接口
 *
 * @author y3tu
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 分页查询
     *
     * @param pageInfo 分页信息 包含每页多少条,当前页数,排序,查询条件
     * @return 分页查询数据
     */
    PageInfo<T> page(PageInfo<T> pageInfo);

    /**
     * 分页查询
     *
     * @param pageInfo 分页信息 包含每页多少条,当前页数,排序,查询条件
     * @param wrapper  查询条件
     * @return 分页查询数据
     */
    PageInfo<T> page(PageInfo<T> pageInfo, Wrapper<T> wrapper);

    /**
     * 新增-用雪花算法生成主键Id
     *
     * @param entity 数据实体
     * @return 是否新增成功
     */
    boolean saveBySnowflakeId(T entity);

    /**
     * 新增-用雪花算法生成主键Id
     *
     * @param entity       新增数据
     * @param workerId     终端ID
     * @param dataCenterId 数据中心ID
     * @return 是否新增成功
     */
    boolean saveBySnowflakeId(T entity, int workerId, int dataCenterId);

    /**
     * 批量新增-用雪花算法生成主键Id
     *
     * @param entityList 新增数据集合
     * @param batchSize  一个批次的数量
     * @return 是否新增成功
     */
    boolean saveBatchBySnowflakeId(Collection<T> entityList, int batchSize);

    /**
     * 批量新增-用雪花算法生成主键Id
     *
     * @param entityList   新增数据集合
     * @param batchSize    一个批次的数量
     * @param workerId     终端ID
     * @param dataCenterId 数据中心ID
     * @return 是否新增成功
     */
    boolean saveBatchBySnowflakeId(Collection<T> entityList, int batchSize, int workerId, int dataCenterId);
}
