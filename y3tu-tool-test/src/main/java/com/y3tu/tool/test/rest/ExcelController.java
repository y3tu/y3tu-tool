package com.y3tu.tool.test.rest;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.y3tu.tool.test.dto.UserDto;
import com.y3tu.tool.web.excel.ExcelUtil;
import com.y3tu.tool.web.sql.DataHandler;
import com.y3tu.tool.web.sql.SqlUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * excel测试
 *
 * @author y3tu
 */
@RestController
@RequestMapping("excel")
public class ExcelController {

    @GetMapping("export")
    public void testExportExcel(HttpServletResponse response) throws Exception {

        String selectSql = "select id,name,age from user where $MOD(id,?)=?";
        int count = SqlUtil.count("select id,name,age from user", "support");
        response = ExcelUtil.decorateResponse("测试", ExcelTypeEnum.XLSX, response);
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream());
        ExcelWriter excelWriter = excelWriterBuilder.build();
        WriteSheet sheet = EasyExcel.writerSheet(0, "测试").build();
        response.getOutputStream().flush();
        SqlUtil.dataPageHandler(true, 500, selectSql, "support", UserDto.class, null, new DataHandler<UserDto>() {
            @Override
            public void handle(List<UserDto> dataList) {
                excelWriter.write(dataList, sheet);
            }
        });
        excelWriter.finish();
    }

}
