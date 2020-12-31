package com.y3tu.tool.report.service;

import com.y3tu.tool.report.domain.DictSql;
import com.y3tu.tool.web.base.jpa.BaseService;

/**
 * @author y3tu
 */
public interface DictSqlService extends BaseService<DictSql> {
    /**
     * 根据dictId获取字典sql
     *
     * @param dictId
     * @return
     */
    DictSql getByDictId(long dictId);
}
