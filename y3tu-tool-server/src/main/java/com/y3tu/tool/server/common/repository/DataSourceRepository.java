package com.y3tu.tool.server.common.repository;

import com.y3tu.tool.server.common.entity.domain.DataSource;
import com.y3tu.tool.web.base.jpa.BaseRepository;

import java.util.List;

/**
 * @author y3tu
 */
public interface DataSourceRepository extends BaseRepository<DataSource> {
    /**
     * 根据名称获取数据源
     *
     * @param name
     * @return
     */
    List<DataSource> getByNameLike(String name);
}
