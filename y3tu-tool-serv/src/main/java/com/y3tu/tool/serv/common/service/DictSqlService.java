package com.y3tu.tool.serv.common.service;

import com.y3tu.tool.serv.common.entity.domain.DictSql;
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
    DictSql getByDictId(int dictId);
}
