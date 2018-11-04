package com.y3tu.tool.web.codegen.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.y3tu.tool.core.io.IoUtil;
import com.y3tu.tool.web.base.pojo.Query;
import com.y3tu.tool.web.codegen.entity.GenConfig;
import com.y3tu.tool.web.codegen.service.GeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 代码生成器
 *
 * @author lengleng
 * @date 2018-07-30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/generator")
public class GeneratorController {
    private final GeneratorService sysGeneratorService;

    /**
     * 列表
     *
     * @param params 参数集
     * @return 数据库表
     */
    @GetMapping("/page")
    public Page list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        query.getPage().setRecords(sysGeneratorService.queryTableName());
        return query.getPage();
    }

    /**
     * 生成代码
     */
    @PostMapping("/code")
    public void code(@RequestBody GenConfig genConfig, HttpServletResponse response) throws IOException {
        byte[] data = sysGeneratorService.generatorCode(genConfig);

        response.reset();
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip", genConfig.getTableName()));
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
    }
}
