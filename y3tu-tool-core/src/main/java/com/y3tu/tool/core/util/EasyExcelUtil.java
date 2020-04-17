package com.y3tu.tool.core.util;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel工具类
 * 使用easyExcel作为实现，版本2.1.7
 * https://github.com/alibaba/easyexcel
 * 更多用法参考官方文档 https://www.yuque.com/easyexcel/doc/easyexcel
 *
 * @author y3tu
 */
public class EasyExcelUtil {

    /**
     * 浏览器导出excel文件
     *
     * @param fileName  文件名
     * @param sheetName sheet页名称
     * @param list      导出的数据
     * @param clazz     导出的实体类型
     * @param response  响应请求
     * @throws IOException
     */
    public static void downExcel(String fileName, String sheetName, List<?> list, Class clazz, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(list);
    }

}
