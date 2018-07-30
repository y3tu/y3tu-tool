package com.y3tu.tool.poi.excel;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/7/29
 */
public class ExcelWriterTest {

    @Test
    public void write() throws IOException {
         ExcelWriter writer = new ExcelWriter("/Users/yxy/work/writeTest.xls");

        // 跳过当前行，既第一行，非必须，在此演示用
       // writer.passCurrentRow();
        // 合并单元格后的标题行，使用默认标题样式
       // writer.merge(row1.size() - 1, "测试标题");
        // 一次性写出内容，使用默认样式
        //writer.write(rows);

        Map map = new HashMap();
        map.put("ddd","xxx");
        List<String> list = new ArrayList<>();
        list.add("test");
        list.add("test2");
        writer.writeHeadRow(list);
        writer.autoSizeColumn(0, true);
        // 关闭writer，释放内存
        writer.close();
    }
}