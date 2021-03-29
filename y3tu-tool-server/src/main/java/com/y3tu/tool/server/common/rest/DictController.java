package com.y3tu.tool.server.common.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.server.common.entity.domain.Dict;
import com.y3tu.tool.server.common.entity.domain.DictData;
import com.y3tu.tool.server.common.entity.domain.DictSql;
import com.y3tu.tool.server.common.service.DictDataService;
import com.y3tu.tool.server.common.service.DictService;
import com.y3tu.tool.server.common.service.DictSqlService;
import com.y3tu.tool.web.base.jpa.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author y3tu
 */
@RestController
@RequestMapping("${y3tu.tool.report.urlPattern:y3tu-tool-report}/dict")
public class DictController {

    @Autowired
    DictService dictService;

    @Autowired
    DictDataService dictDataService;

    @Autowired
    DictSqlService dictSqlService;

    @PostMapping("/dictPage")
    public R dictPage(@RequestBody PageInfo<Dict> pageInfo) {
        return R.success(dictService.page(pageInfo));
    }

    @PostMapping("/dictDataPage")
    public R dictDataPage(@RequestBody PageInfo<DictData> pageInfo) {
        return R.success(dictDataService.page(pageInfo));
    }

    @GetMapping("/getDictSql/{dictId}")
    public R getDictSql(@PathVariable int dictId) {
        DictSql dictSql = dictSqlService.getByDictId(dictId);
        return R.success(dictSql);
    }

    @GetMapping("/getDictDataByCode/{code}")
    public R getDictDataByCode(@PathVariable String code) {
        Dict dict = dictService.getByCode(code);
        if (dict == null) {
            return R.error(String.format("字典编码[%s]不存在", code));
        }
        return R.success(dictService.getDictData(dict.getCode()));
    }

    @GetMapping("/getDictDataByDictName/{name}")
    public R getDictDataByDictName(@PathVariable String name) {
        List<Dict> dict = dictService.getByName(name);
        if (dict.isEmpty()) {
            return R.error(String.format("字典名称[%s]不存在", name));
        }
        return R.success(dictService.getDictData(dict.get(0).getCode()));
    }

    @GetMapping("/getAllDict")
    public R getAllDict() {
        Dict dict = new Dict();
        return R.success(dictService.getAll(dict));
    }

    @GetMapping("/getDictByNameOrCode/{param}")
    public R getDictByNameOrCode(@PathVariable String param) {
        return R.success(dictService.getByNameOrCode(param));
    }

    @PostMapping("/createDict")
    public R createDict(@RequestBody Dict dict) {
        Dict dictOne = dictService.getByCode(dict.getCode());
        if (dictOne != null) {
            return R.error(String.format("字典编码[%s]已存在", dictOne.getCode()));
        }
        dict.setCreateTime(new Date());
        dictService.create(dict);
        return R.success();
    }

    @PostMapping("/createDictData")
    public R createDictData(@RequestBody DictData dictData) {
        Dict dict = (Dict) dictService.getById(dictData.getDictId());
        if (dict == null) {
            return R.error(String.format("字典ID[%s]不存在", dictData.getDictId()));
        }
        dictDataService.create(dictData);
        return R.success();
    }

    @PostMapping("/saveDictSql")
    public R saveDictSql(@RequestBody DictSql dictSql) {
        if (dictSql.getId() != null && dictSql.getId() != 0) {
            dictSql.setUpdateTime(new Date());
            dictSqlService.update(dictSql);
        } else {
            dictSql.setCreateTime(new Date());
            dictSqlService.create(dictSql);
        }
        return R.success();
    }

    @PostMapping("/updateDict")
    public R updateDict(@RequestBody Dict dict) {
        Dict old = (Dict) dictService.getById(dict.getId());
        // 若code修改需判断唯一
        if (!old.getCode().equals(dict.getCode()) && dictService.getByCode(dict.getCode()) != null) {
            return R.error(String.format("字典编码[%s]已存在", dict.getCode()));
        }
        dict.setUpdateTime(new Date());
        dictService.update(dict);
        return R.success();
    }

    @PostMapping("/updateDictData")
    public R updateDictData(@RequestBody DictData dictData) {
        dictDataService.update(dictData);
        return R.success();
    }

    @GetMapping("/deleteDict/{id}")
    public R deleteDict(@PathVariable int id) {
        dictService.delete(id);
        return R.success();
    }

    @GetMapping("/deleteDictData/{id}")
    public R deleteDictData(@PathVariable int id) {
        dictDataService.delete(id);
        return R.success();
    }

}
