package com.y3tu.tool.report.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.domain.DataSource;
import com.y3tu.tool.report.service.DataSourceService;
import com.y3tu.tool.web.base.jpa.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 数据源
 *
 * @author y3tu
 */
@RestController
@RequestMapping("${y3tu.tool.report.urlPattern:y3tu-tool-report}/dataSource")
public class DataSourceController {

    @Autowired
    DataSourceService dataSourceService;

    @PostMapping("page")
    public R page(@RequestBody PageInfo<DataSource> pageInfo) {
        return R.success(dataSourceService.page(pageInfo));
    }

    @GetMapping("get/{id}")
    public R get(@PathVariable int id) {
        return R.success(dataSourceService.findById(id));
    }

    @PostMapping("create")
    public R create(@RequestBody DataSource dataSource) {
        dataSource.setCreateTime(new Date());
        dataSourceService.create(dataSource);
        return R.success();
    }

    @PostMapping("update")
    public R update(@RequestBody DataSource dataSource) {
        dataSource.setUpdateTime(new Date());
        dataSourceService.update(dataSource);
        return R.success();
    }

    @GetMapping("del/{id}")
    public R del(@PathVariable long id) {
        dataSourceService.delete(id);
        return R.success();
    }

    @GetMapping("testConnect/{id}")
    public R testConnect(@PathVariable long id) {
        DataSource dataSource = (DataSource) dataSourceService.findById(id);
        return R.success(dataSourceService.testConnection(dataSource));
    }
}
