package com.y3tu.tool.web.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.thread.ThreadUtil;
import com.y3tu.tool.web.excel.listener.CustomExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Excel工具类
 * 使用easyExcel作为实现，版本2.1.7
 * https://github.com/alibaba/easyexcel
 * 更多用法参考官方文档 https://www.yuque.com/easyexcel/doc/easyexcel
 *
 * @author y3tu
 */
@Slf4j
public class ExcelUtil extends EasyExcel {

    /**
     * xlsx分页大小
     */
    public static final int XLSX_PAGE_SIZE = 50000;
    /**
     * xlsx每个sheet页最大数据条数
     */
    public static final int XLSX_SHEET_MAX_ROW = 1000000;
    /**
     * xls分页大小
     */
    public static final int XLS_PAGE_SIZE = 50000;
    /**
     * xls每个sheet页最大数据条数
     */
    public static final int XLS_SHEET_MAX_ROW = 60000;


    /**
     * 装饰响应对象，适应浏览器下载
     *
     * @param fileName  文件名
     * @param excelType excel文件类型
     * @param response  浏览器响应
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
     * 创建ExcelWriter对象
     *
     * @param fileName      文件名
     * @param clazz         导出的实体类型
     * @param header        自定义表格头
     * @param excelTypeEnum excel文件类型
     * @param response      浏览器响应
     * @return
     */
    public static ExcelWriter buildExcelWriter(String fileName, Class clazz, List<List<String>> header, ExcelTypeEnum excelTypeEnum, HttpServletResponse response) {
        try {
            ExcelUtil.decorateResponse(fileName, excelTypeEnum, response);
            ExcelWriterBuilder excelWriterBuilder = null;
            if (clazz != null) {
                excelWriterBuilder = EasyExcel.write(response.getOutputStream()).head(header).autoCloseStream(true);
            } else {
                excelWriterBuilder = EasyExcel.write(response.getOutputStream(), clazz).head(header).autoCloseStream(true);
            }
            ExcelWriter excelWriter = excelWriterBuilder.build();
            return excelWriter;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ToolException("创建ExcelWriter异常！" + e.getMessage(), e);
        }
    }


    /**
     * 单线程分页查询、写数据,避免导出大数据量时OOM
     *
     * @param fileName      文件名
     * @param sheetName     sheet页名称
     * @param clazz         导出的实体类型
     * @param header        自定义表格头
     * @param excelTypeEnum 导出文件后缀
     * @param excelPageData 执行分页查询的方法
     * @param response      响应信息
     */
    public static void downExcelByPage(String fileName, String sheetName, Class clazz, List<List<String>> header, ExcelTypeEnum excelTypeEnum, ExcelPageData excelPageData, HttpServletResponse response) {
        try {
            ExcelWriter excelWriter = buildExcelWriter(fileName, clazz, header, excelTypeEnum, response);
            //使用默认的 xlsx, page size 10000, sheet max row 1000000
            int pageSize = XLSX_PAGE_SIZE;
            int sheetMaxRow = XLSX_SHEET_MAX_ROW;
            if (excelTypeEnum == ExcelTypeEnum.XLS) {
                pageSize = XLS_PAGE_SIZE;
                sheetMaxRow = XLS_SHEET_MAX_ROW;
            }
            int startNbr = 0;
            int sheetNbr = 0;
            int count = 0;
            // 分页写数据
            WriteSheet sheet = EasyExcel.writerSheet(sheetNbr, sheetName).build();

            //导出开始时间 单位秒
            long startTime = System.currentTimeMillis() / 1000;

            while (true) {
                //分页获取数据
                List data = excelPageData.queryDataByPage(startNbr, pageSize);
                if (data == null || data.size() == 0) {
                    //数据获取结束
                    break;
                }
                if (count + data.size() > sheetMaxRow) {
                    //如果超出一个sheet的最大数据条数,需另建sheet页
                    sheet = EasyExcel.writerSheet(++sheetNbr, sheetName + sheetNbr).build();
                    //清空计数
                    count = 0;
                }
                count += data.size();
                excelWriter.write(data, sheet);
                data.clear();
                startNbr += pageSize;

            }
            //关闭流
            excelWriter.finish();
            long endTime = System.currentTimeMillis() / 1000;
            log.info("导出耗时：" + (endTime - startTime) + " 秒");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ToolException("分页导出数据到excel异常", e.getMessage());
        }

    }

    /**
     * 多线程分页查询，单线程写数据到文件
     *
     * @param poolSize      多线程线程池大小
     * @param fileName      文件名
     * @param sheetName     sheet页名称
     * @param clazz         导出的实体类型
     * @param header        自定义表格头
     * @param excelTypeEnum 导出文件后缀
     * @param excelPageData 执行分页查询的方法
     * @param response      响应信息
     */
    public static void downExcelByThreadAndPage(int poolSize, String fileName, String sheetName, Class clazz, List<List<String>> header, ExcelTypeEnum excelTypeEnum, ExcelPageData excelPageData, HttpServletResponse response) {
        try {
            // 根据数据读写速度来调整，一般来说读的逻辑复杂，比较慢，如果读比写快，这里设为1
            int N = 4;
            // 大小设置为2就可以，作为缓冲
            BlockingQueue<List> queue = new ArrayBlockingQueue<>(4);
            // 数据从0开始
            AtomicInteger start = new AtomicInteger(0);
            ThreadFactory threadFactory = ThreadUtil.newNamedThreadFactory("多线程分页查询导出", true);
            ExecutorService executorService = ThreadUtil.newFixedExecutor(poolSize, threadFactory);
            ExcelWriter excelWriter = buildExcelWriter(fileName, clazz, header, excelTypeEnum, response);
            //默认分页大小1000条
            int pageSize = XLSX_PAGE_SIZE;
            int sheetMaxRow = XLSX_SHEET_MAX_ROW;
            if (excelTypeEnum == ExcelTypeEnum.XLS) {
                pageSize = XLS_PAGE_SIZE;
                sheetMaxRow = XLS_SHEET_MAX_ROW;
            }
            final int size = pageSize;
            final int maxRow = sheetMaxRow;

            //导出开始时间 单位秒
            long startTime = System.currentTimeMillis() / 1000;

            //多线程获取分页数据
            for (int i = 0; i < N; i++) {
                executorService.submit(() -> {
                    while (true) {
                        //自增
                        int startNbr = start.getAndAdd(size);
                        try {
                            List list = excelPageData.queryDataByPage(startNbr, size);
                            if (CollectionUtil.isEmpty(list)) {
                                //数据读取完成,读到没数据也要放入空集合
                                queue.put(Collections.EMPTY_LIST);
                                break;
                            }
                            queue.put(list);
                        } catch (Exception e) {
                            //异常情况也要放入空集合，防止写线程无法退出循环
                            try {
                                queue.put(Collections.EMPTY_LIST);
                            } catch (InterruptedException interruptedException) {
                                log.error(e.getMessage(), e);
                            }
                            log.error(e.getMessage(), e);
                            throw new ToolException("获取数据异常!", e.getMessage());
                        }
                    }
                });
            }
            //单线程写数据到writer中
            Future<?> submit = executorService.submit(() -> {
                int count = 0;
                int emptyCount = 0;
                int sheetNbr = 0;
                WriteSheet sheet = EasyExcel.writerSheet(sheetNbr, sheetName).build();
                while (true) {
                    //分页查询出的数据
                    List data = null;
                    try {
                        data = queue.take();

                        if (CollectionUtil.isEmpty(data)) {
                            emptyCount++;
                            // 当获取到两次空集合时，说明已经读完
                            if (emptyCount == N) {
                                break;
                            }
                            continue;
                        }
                        if (count + data.size() > maxRow) {
                            //如果超出一个sheet的最大数据条数,需另建sheet页
                            sheet = EasyExcel.writerSheet(++sheetNbr, sheetName + sheetNbr).build();
                            //清空计数
                            count = 0;
                        }
                        count += data.size();
                        excelWriter.write(data, sheet);
                        data.clear();

                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw new ToolException("获取数据异常!", e.getMessage());
                    }
                }
                //关闭流
                excelWriter.finish();
            });

            //阻塞，等到数据导出完成
            submit.get();
            long endTime = System.currentTimeMillis() / 1000;
            log.info("导出耗时：" + (endTime - startTime) + " 秒");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ToolException("分页导出数据到excel异常", e.getMessage());
        }
    }

    /**
     * 浏览器导出excel文件
     *
     * @param fileName  文件名
     * @param sheetName sheet页名称
     * @param list      导出的数据
     * @param clazz     导出的实体类型
     * @param header    自定义表格头
     * @param excelType excel文件类型
     * @param response  响应请求
     */
    public static void downExcel(String fileName, String sheetName, List<?> list, Class clazz, List<List<String>> header, ExcelTypeEnum excelType, HttpServletResponse response) {
        try {
            decorateResponse(fileName, excelType, response);
            EasyExcel.write(response.getOutputStream(), clazz).head(header).sheet(sheetName).doWrite(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ToolException("导出excel文件异常:" + e.getMessage());
        }
    }


    /**
     * 读取web上传excel文件
     *
     * @param file     上传文件
     * @param clazz    转换的实体对象
     * @param listener 读取数据处理
     */
    public static void readExcel(MultipartFile file, Class clazz, CustomExcelListener listener) {
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(file.getInputStream(), clazz, listener).build();
            ReadSheet readSheet = EasyExcel.readSheet().build();
            excelReader.read(readSheet);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ToolException("读取excel文件失败");
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }

    }
}
