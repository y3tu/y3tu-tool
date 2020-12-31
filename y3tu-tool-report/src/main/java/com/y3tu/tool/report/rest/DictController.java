package com.y3tu.tool.report.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.domain.Dict;
import com.y3tu.tool.report.domain.DictData;
import com.y3tu.tool.report.domain.DictSql;
import com.y3tu.tool.report.service.DictDataService;
import com.y3tu.tool.report.service.DictService;
import com.y3tu.tool.report.service.DictSqlService;
import com.y3tu.tool.web.base.jpa.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @PostMapping("/createDict")
    public R createDict(@Valid Dict dict) {
        Dict dictOne = dictService.getByCode(dict.getCode());
        if (dictOne != null) {
            return R.error(String.format("字典编码[%s]已存在", dictOne.getCode()));
        }
        dictService.create(dict);
        return R.success();
    }

    @PostMapping("/createDictData")
    public R createDictData(@Valid DictData dictData) {
        Dict dict = (Dict) dictService.findById(dictData.getDictId());
        if (dict == null) {
            return R.error(String.format("字典ID[%s]不存在", dictData.getDictId()));
        }
        dictDataService.create(dictData);
        return R.success();
    }

    @PostMapping("/createDictSql")
    public R createDictSql(@Valid DictSql dictSql) {
        dictSqlService.create(dictSql);
        return R.success();
    }

    @PutMapping("/updateDict")
    public R updateDict(@Valid Dict dict) {
        Dict old = (Dict) dictService.findById(dict.getId());
        // 若code修改需判断唯一
        if (!old.getCode().equals(dict.getCode()) && dictService.getByCode(dict.getCode()) != null) {
            return R.error(String.format("字典编码[%s]已存在", dict.getCode()));
        }
        dictService.update(dict);
        return R.success();
    }

    @PutMapping("/updateDictData")
    public R updateDictData(@Valid DictData dictData) {
        dictDataService.update(dictData);
        return R.success();
    }

    @DeleteMapping("/deleteDict")
    public R deleteDict(@RequestBody long[] ids) {
        dictService.delete(ids);
        return R.success();
    }

    @DeleteMapping("/deleteDictData")
    public R deleteDictData(@RequestBody long[] ids) {
        dictDataService.delete(ids);
        return R.success();
    }

    @GetMapping("/getDictSql/{dictId}")
    public R getDictSql(@PathVariable long dictId) {
        DictSql dictSql = dictSqlService.getByDictId(dictId);
        return R.success(dictSql);
    }

    @GetMapping("/getDictData/{code}")
    public R getDictData(@PathVariable String code) {
        Dict dict = dictService.getByCode(code);
        if (dict == null) {
            return R.error(String.format("字典编码[%s]不存在", code));
        }
        return R.success(dictService.getDictData(dict.getCode()));
    }

    @GetMapping("/getDictDataByDictName/{name}")
    public R getDictDataByDictName(@PathVariable String name) {
        Dict dict = dictService.getByName(name);
        if (dict == null) {
            return R.error(String.format("字典名称[%s]不存在", name));
        }
        return R.success(dictService.getDictData(dict.getCode()));
    }

}
