package com.y3tu.tool.report.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.entity.domain.GenerationLog;
import com.y3tu.tool.report.service.GenerationLogService;
import com.y3tu.tool.web.base.jpa.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报表生成日志
 *
 * @author y3tu
 */
@RestController
@RequestMapping("${y3tu.tool.report.urlPattern:y3tu-tool-report}/generationLog")
public class GenerationLogController {
    @Autowired
    GenerationLogService generationLogService;

    @PostMapping("page")
    public R page(@RequestBody PageInfo<GenerationLog> pageInfo) {
        return R.success(generationLogService.page(pageInfo));
    }

}
