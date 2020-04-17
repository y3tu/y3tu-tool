package com.y3tu.tool.web.excel;

/**
 * 分页参数枚举
 *
 * @author y3tu
 */
public enum ExcelPageEnum {

    XLS(10000, 60000),
    XLSX(10000, 1000000);

    private int pageSize;
    private int sheetMaxRow;

    ExcelPageEnum(int pageSize, int sheetMaxRow) {
        this.pageSize = pageSize;
        this.sheetMaxRow = sheetMaxRow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getSheetMaxRow() {
        return sheetMaxRow;
    }
}
