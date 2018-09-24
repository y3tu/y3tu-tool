package com.y3tu.tool.poi.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.io.IORuntimeException;
import com.y3tu.tool.core.io.IOUtil;
import com.y3tu.tool.poi.exceptions.POIException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Excel工作簿{@link Workbook}相关工具类
 *
 * @author looly
 */
public class WorkbookUtil {

    /**
     * 创建或加载工作簿
     *
     * @param excelFilePath Excel文件路径，绝对路径或相对于ClassPath路径
     * @return {@link Workbook}
     */
    public static Workbook createBook(String excelFilePath) {
        return createBook(FileUtil.file(excelFilePath), null);
    }

    /**
     * 创建或加载工作簿
     *
     * @param excelFile Excel文件
     * @return {@link Workbook}
     */
    public static Workbook createBook(File excelFile) {
        return createBook(excelFile, null);
    }

    /**
     * 创建或加载工作簿，只读模式
     *
     * @param excelFile Excel文件
     * @param password  Excel工作簿密码，如果无密码传{@code null}
     * @return {@link Workbook}
     */
    public static Workbook createBook(File excelFile, String password) {
        try {
            return WorkbookFactory.create(excelFile, password);
        } catch (Exception e) {
            throw new POIException(e);
        }
    }

    /**
     * 创建或加载工作簿
     *
     * @param in             Excel输入流
     * @param closeAfterRead 读取结束是否关闭流
     * @return {@link Workbook}
     */
    public static Workbook createBook(InputStream in, boolean closeAfterRead) {
        return createBook(in, null, closeAfterRead);
    }

    /**
     * 创建或加载工作簿
     *
     * @param in             Excel输入流
     * @param password       密码
     * @param closeAfterRead 读取结束是否关闭流
     * @return {@link Workbook}
     */
    public static Workbook createBook(InputStream in, String password, boolean closeAfterRead) {
        try {
            return WorkbookFactory.create(IOUtil.toMarkSupportStream(in), password);
        } catch (Exception e) {
            throw new POIException(e);
        } finally {
            if (closeAfterRead) {
                IOUtil.close(in);
            }
        }
    }

    /**
     * 根据文件类型创建新的工作簿，文件路径
     *
     * @param isXlsx 是否为xlsx格式的Excel
     * @return {@link Workbook}
     */
    public static Workbook createBook(boolean isXlsx) {
        Workbook workbook;
        if (isXlsx) {
            workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        } else {
            workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook();
        }
        return workbook;
    }

    /**
     * 创建或加载SXSSFWorkbook工作簿
     *
     * @param excelFilePath Excel文件路径，绝对路径或相对于ClassPath路径
     * @return {@link SXSSFWorkbook}
     */
    public static SXSSFWorkbook createSXSSFBook(String excelFilePath) {
        return createSXSSFBook(FileUtil.file(excelFilePath), null);
    }

    /**
     * 创建或加载SXSSFWorkbook工作簿
     *
     * @param excelFile Excel文件
     * @return {@link SXSSFWorkbook}
     */
    public static SXSSFWorkbook createSXSSFBook(File excelFile) {
        return createSXSSFBook(excelFile, null);
    }

    /**
     * 创建或加载SXSSFWorkbook工作簿，只读模式
     *
     * @param excelFile Excel文件
     * @param password  Excel工作簿密码，如果无密码传{@code null}
     * @return {@link SXSSFWorkbook}
     */
    public static SXSSFWorkbook createSXSSFBook(File excelFile, String password) {
        return toSXSSFBook(createBook(excelFile, password));
    }

    /**
     * 创建或加载SXSSFWorkbook工作簿
     *
     * @param in             Excel输入流
     * @param closeAfterRead 读取结束是否关闭流
     * @return {@link SXSSFWorkbook}
     */
    public static SXSSFWorkbook createSXSSFBook(InputStream in, boolean closeAfterRead) {
        return createSXSSFBook(in, null, closeAfterRead);
    }

    /**
     * 创建或加载SXSSFWorkbook工作簿
     *
     * @param in             Excel输入流
     * @param password       密码
     * @param closeAfterRead 读取结束是否关闭流
     * @return {@link SXSSFWorkbook}
     */
    public static SXSSFWorkbook createSXSSFBook(InputStream in, String password, boolean closeAfterRead) {
        return toSXSSFBook(createBook(in, password, closeAfterRead));
    }

    /**
     * 创建SXSSFWorkbook，用于大批量数据写出
     *
     * @return {@link SXSSFWorkbook}
     */
    public static SXSSFWorkbook createSXSSFBook() {
        return new SXSSFWorkbook();
    }

    /**
     * 创建SXSSFWorkbook，用于大批量数据写出
     *
     * @param rowAccessWindowSize 在内存中的行数
     * @return {@link Workbook}
     */
    public static SXSSFWorkbook createSXSSFBook(int rowAccessWindowSize) {
        return new SXSSFWorkbook(rowAccessWindowSize);
    }

    /**
     * 将Excel Workbook刷出到输出流，不关闭流
     *
     * @param book {@link Workbook}
     * @param out  输出流
     * @throws IORuntimeException IO异常
     */
    public static void writeBook(Workbook book, OutputStream out) throws IORuntimeException {
        try {
            book.write(out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    //-------------------------------------------------------------------------------------------------------- Private method start

    /**
     * 将普通工作簿转换为SXSSFWorkbook
     *
     * @param book 工作簿
     * @return SXSSFWorkbook
     */
    private static SXSSFWorkbook toSXSSFBook(Workbook book) {
        if (book instanceof SXSSFWorkbook) {
            return (SXSSFWorkbook) book;
        }
        if (book instanceof XSSFWorkbook) {
            return new SXSSFWorkbook((XSSFWorkbook) book);
        }
        throw new POIException("The input is not a [xlsx] format.");
    }
    //-------------------------------------------------------------------------------------------------------- Private method end
}
