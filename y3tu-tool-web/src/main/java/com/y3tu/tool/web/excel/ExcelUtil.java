package com.y3tu.tool.web.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;

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
public class ExcelUtil extends EasyExcel {

    /**
     * 装饰响应对象，已适应浏览器下载
     *
     * @param fileName  文件名
     * @param excelType excel文件类型
     * @param response  响应
     * @return HttpServletResponse
     */
    public static HttpServletResponse decorateResponse(String fileName, ExcelTypeEnum excelType, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyExcel没有关系
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + excelType.getValue());
        return response;
    }

    /**
     * 浏览器导出excel文件
     *
     * @param fileName  文件名
     * @param sheetName sheet页名称
     * @param list      导出的数据
     * @param clazz     导出的实体类型
     * @param excelType excel文件类型
     * @param response  响应请求
     * @throws IOException
     */
    public static void downExcel(String fileName, String sheetName, List<?> list, Class clazz, ExcelTypeEnum excelType, HttpServletResponse response) throws Exception {
        response = decorateResponse(fileName, excelType, response);
        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(list);
    }

    /**
     * 分页查询、分批次写数据,避免导出大数据量时OOM
     *
     * @param excelWriter writer对象
     * @param sheetName   sheet页名称
     * @param startPage   开始页
     * @param totalCount  数据总数
     * @param collect     分页查询方法
     */
    public static void pageWrite(ExcelWriter excelWriter, String sheetName, int startPage, long totalCount, ExcelPageCollect collect) {

        //使用默认的 xlsx, page size 10000, sheet max row 1000000
        int pageSize = ExcelPageEnum.XLSX.getPageSize();
        int sheetMaxRow = ExcelPageEnum.XLSX.getSheetMaxRow();

        ExcelTypeEnum excelType = excelWriter.writeContext().writeWorkbookHolder().getExcelType();
        boolean isXls = excelType != null && ExcelTypeEnum.XLS.getValue().equals(excelType.getValue());
        if (isXls) {
            pageSize = ExcelPageEnum.XLS.getPageSize();
            sheetMaxRow = ExcelPageEnum.XLS.getSheetMaxRow();
        }
        // 计算 page count, sheet count
        long pageCount = (totalCount - 1) / pageSize + 1;
        long sheetCount = (totalCount - 1) / sheetMaxRow + 1;

        int currentPage = startPage;
        // 分页写数据
        WriteSheet sheet = null;
        for (int i = 0; i < sheetCount; i++) {
            sheet = EasyExcel.writerSheet(i, sheetName + i).build();
            for (int j = 0; j < (sheetMaxRow / pageSize); j++) {
                excelWriter.write(collect.data(currentPage++, pageSize), sheet);
                if (currentPage >= pageCount) {
                    break;
                }
            }
        }

        //关闭流
        excelWriter.finish();

    }
}