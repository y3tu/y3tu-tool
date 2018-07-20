package com.y3tu.tool.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;


/**
 * https://my.oschina.net/LinkedBear/blog/1785242
 * https://blog.csdn.net/liu4819627/article/details/51150748
 * @author y3tu
 * @date 2018/6/19
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        File file = new File("/Users/yxy/work/test.xls");
        file.createNewFile();
        //1. 创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();

        //1.1 创建单元格样式
        HSSFCellStyle alignCenterStyle = workbook.createCellStyle();
       // alignCenterStyle.setAlignment(HorizontalAlignment.CENTER);
        //表名样式：水平、垂直居中
        HSSFCellStyle font24Style = workbook.createCellStyle();
        //font24Style.setAlignment(HorizontalAlignment.CENTER);
        //font24Style.setVerticalAlignment(VerticalAlignment.CENTER);
        //24号字体
        HSSFFont font24 = workbook.createFont();
        font24.setFontHeightInPoints((short) 24);
        font24Style.setFont(font24);


        //2. 创建一个工作表Sheet
        HSSFSheet sheet = workbook.createSheet("新工作表");
        //2.1 设置列宽 -- 列宽是属于一个工作表的特征
        sheet.setColumnWidth(0, 6 * 2 * 256);
        sheet.setColumnWidth(1, 6 * 2 * 256);
        sheet.setColumnWidth(2, 6 * 2 * 256);
        sheet.setColumnWidth(3, 6 * 2 * 256);
        sheet.setColumnWidth(4, 6 * 2 * 256);
        sheet.setColumnWidth(5, 6 * 2 * 256);
        sheet.setColumnWidth(6, 6 * 2 * 256);

        //3.1 创建一个行
        int row = 0;
        HSSFRow tableHeadRow = sheet.createRow(row++);
        //3.2 在这个行中，创建一行单元格
        for (int i = 0; i < 7; i++) {
            tableHeadRow.createCell(i);
        }
        tableHeadRow.setRowStyle(font24Style);
        //再创建两个行
        sheet.createRow(row++);
        sheet.createRow(row++);
        //3.3 设置该单元格的内容
        HSSFCell tableHeadCell = tableHeadRow.getCell(0);
        tableHeadCell.setCellValue("表名");
        tableHeadCell.setCellStyle(font24Style);
        //表头的数据应该是很多单元格的合并、居中
        //四个参数：开始行，结束行，开始列，结束列
        sheet.addMergedRegion(new CellRangeAddress(0, row - 1, 0, 6));

        //4. 写表头
        HSSFRow headRow = sheet.createRow(row);
        headRow.setHeightInPoints(20);
        headRow.createCell(0).setCellValue("第0列");
        headRow.createCell(1).setCellValue("第1列");
        headRow.createCell(2).setCellValue("第2列");
        headRow.createCell(3).setCellValue("第3列");

        sheet.addMergedRegion(new CellRangeAddress(3, 3, 4, 6));

        //Workbook写入file中
        //workbook.write(file);
        workbook.close();
    }
}
