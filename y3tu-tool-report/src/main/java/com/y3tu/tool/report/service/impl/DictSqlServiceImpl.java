package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.report.domain.DictSql;
import com.y3tu.tool.report.repository.DictSqlRepository;
import com.y3tu.tool.report.service.DictSqlService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author y3tu
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictSqlServiceImpl extends BaseServiceImpl<DictSqlRepository, DictSql> implements DictSqlService {

    @Override
    public DictSql getByDictId(long dictId) {
        return repository.findByDictId(dictId);
    }
}
