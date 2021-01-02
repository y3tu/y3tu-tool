package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.core.util.ObjectUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.report.entity.domain.Dict;
import com.y3tu.tool.report.entity.domain.Dict.DictType;
import com.y3tu.tool.report.entity.domain.DictData;
import com.y3tu.tool.report.entity.domain.DictSql;
import com.y3tu.tool.report.emums.DataStatusEnum;
import com.y3tu.tool.report.exception.ReportException;
import com.y3tu.tool.report.repository.DictDataRepository;
import com.y3tu.tool.report.repository.DictRepository;
import com.y3tu.tool.report.repository.DictSqlRepository;
import com.y3tu.tool.report.service.DictService;
import com.y3tu.tool.report.util.DataSourceUtil;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author y3tu
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends BaseServiceImpl<DictRepository, Dict> implements DictService {

    @Autowired
    DictDataRepository dictDataRepository;
    @Autowired
    DictSqlRepository dictSqlRepository;


    @Override
    public Dict getByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Dict getByName(String name) {
        return repository.getByNameLike(name);
    }

    @Override
    public Dict getByNameOrCode(String param) {
        return repository.getByNameLikeOrCodeLike("%" + param + "%", "%" + param + "%");
    }

    @Override
    public List<DictData> getDictData(String code, Object... targetValue) {
        Dict dict = repository.findByCode(code);
        if (dict == null) {
            throw new ReportException(String.format("字典编码[s]不存在", code));
        }

        if (DictType.DATA.getValue() == dict.getType()) {
            //普通字典
            List<DictData> dictDataList = dictDataRepository.findAllByDictIdAndStatus(dict.getId(), DataStatusEnum.NORMAL.getValue());

            if (ObjectUtil.isNotEmpty(targetValue)) {
                for (DictData dictData : dictDataList) {
                    if (targetValue.toString().equals(dictData.getValue())) {
                        dictDataList.clear();
                        dictDataList.add(dictData);
                        return dictDataList;
                    }
                }
            } else {
                return dictDataList;
            }
        } else if (DictType.SQL.getValue() == dict.getType()) {
            //sql字典
            DictSql dictSql = dictSqlRepository.findByDictId(dict.getId());
            if (dictSql == null) {
                throw new ReportException(String.format("字典[%s]不存在", code));
            } else if (DataStatusEnum.DISABLE.getValue() == dictSql.getStatus()) {
                throw new ReportException(String.format("字典[%s]被禁用", code));
            }

            String sqlText = dictSql.getQuerySql();
            if (!sqlText.contains("where")) {
                sqlText = sqlText + " where 1=1 ";
            }

            try {
                if (targetValue.length > 0) {
                    String whereColumn = dictSql.getWhereColumn();
                    String[] columns = whereColumn.split(",");
                    for (int i = 0; i < targetValue.length; i++) {
                        sqlText = sqlText + " and " + columns[i] + " in (" + StrUtil.join(",", targetValue) + ") ";
                    }
                }
            } catch (Exception e) {
                throw new ReportException(String.format("字典[%s]解析异常", code), e);
            }
            JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtil.getDataSourceByDsId(dictSql.getDsId()));
            List<DictData> dictDataList = jdbcTemplate.query(sqlText, new BeanPropertyRowMapper(DictData.class));
            return dictDataList;
        }
        return null;
    }
}
