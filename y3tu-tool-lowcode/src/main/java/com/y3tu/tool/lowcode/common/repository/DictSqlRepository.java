package com.y3tu.tool.lowcode.common.repository;

import com.y3tu.tool.lowcode.common.entity.domain.DictSql;
import com.y3tu.tool.web.base.jpa.BaseRepository;

/**
 * @author y3tu
 */
public interface DictSqlRepository extends BaseRepository<DictSql> {

    /**
     * 根据dictId查询dictSql
     *
     * @param dictId
     * @return
     */
    DictSql findByDictId(int dictId);
}
