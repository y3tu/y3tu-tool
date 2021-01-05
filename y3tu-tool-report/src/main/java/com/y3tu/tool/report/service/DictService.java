package com.y3tu.tool.report.service;

import com.y3tu.tool.report.entity.domain.Dict;
import com.y3tu.tool.report.entity.domain.DictData;
import com.y3tu.tool.web.base.jpa.BaseService;

import java.util.List;

/**
 * @author y3tu
 */
public interface DictService extends BaseService<Dict> {

    /**
     * 根据编码获取字典
     *
     * @param code
     * @return
     */
    Dict getByCode(String code);

    /**
     * 根据名称获取字典
     *
     * @param name
     * @return
     */
    List<Dict> getByName(String name);

    /**
     * 根据字典名称或编码获取字典
     *
     * @param param
     * @return
     */
    List<Dict> getByNameOrCode(String param);

    /**
     * 获取字段值
     *
     * @param code        字典编码
     * @param targetValue 筛选值
     * @return
     */
    List<DictData> getDictData(String code, Object... targetValue);

}
