package com.y3tu.tool.report.repository;

import com.y3tu.tool.report.domain.Dict;
import com.y3tu.tool.web.base.jpa.BaseRepository;

/**
 * @author y3tu
 */
public interface DictRepository extends BaseRepository<Dict> {

    /**
     * 根据字典编码查询字典
     *
     * @param code
     * @return
     */
    Dict findByCode(String code);

    /**
     * 根据名称获取字典
     *
     * @param name
     * @return
     */
    Dict getByNameLike(String name);
}
