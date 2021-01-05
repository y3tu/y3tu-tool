package com.y3tu.tool.report.repository;

import com.y3tu.tool.report.entity.domain.DictSql;
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
