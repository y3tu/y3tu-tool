package com.y3tu.tool.test.rest;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.test.dto.UserDto;
import com.y3tu.tool.web.excel.ExcelPageData;
import com.y3tu.tool.web.excel.ExcelUtil;
import com.y3tu.tool.web.excel.listener.CustomExcelListener;
import com.y3tu.tool.web.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel测试
 *
 * @author y3tu
 */
@RestController
@RequestMapping("excel")
@Slf4j
public class ExcelController {

    @GetMapping("downPage")
    public void downPage(HttpServletResponse response) {
        ExcelUtil.downExcelByThreadAndPage(20, "测试", "测试", UserDto.class, ExcelTypeEnum.XLSX, new ExcelPageData<UserDto>() {
            @Override
            public List<UserDto> queryDataByPage(int startNum, int pageSize) {
                String selectSql = "select id,name,age from user limit ?,? ";
                Map paramMap = new HashMap<>(2);
                paramMap.put(1, startNum);
                paramMap.put(2, pageSize);
                List data = SqlUtil.queryList(selectSql, paramMap, UserDto.class, "support");
                return data;
            }
        }, response);
    }

    @PostMapping("upload")
    @ResponseBody
    public R readExcel(MultipartFile file) {
        ExcelUtil.readExcel(file, UserDto.class, new CustomExcelListener<UserDto>() {
            @Override
            protected void handleData(List<UserDto> dataList) {
                dataList.stream().forEach(userDto -> log.info(userDto.getName()));
            }
        });
        return R.success();
    }


}
