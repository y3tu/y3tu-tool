package com.y3tu.tool.poi;


import com.y3tu.tool.core.exception.UtilException;
import com.y3tu.tool.core.reflect.ClassLoaderUtil;

/**
 * POI引入检查器
 *
 * @author looly
 */
public class PoiChecker {

    /**
     * 检查POI包的引入情况
     */
    public static void checkPoiImport() {
        try {
            Class.forName("org.apache.poi.ss.usermodel.Workbook", false, ClassLoaderUtil.getClassLoader());
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new UtilException(e, "You need to add POI dependency 'poi-ooxml' to your project, and version >= 3.17");
        }
    }

    /**
     * 当对应的包或类未找到时，抛出指定异常
     */
    public static UtilException transError(NoClassDefFoundError e) {
        switch (e.getMessage()) {
            case "org/apache/poi/ss/usermodel/Workbook":
                return new UtilException(e, "You need to add POI dependency 'poi-ooxml' to your project, and version >= 3.17");
            case "org/apache/poi/poifs/filesystem/FileMagic":
                return new UtilException(e, "You need to add POI dependency 'poi-ooxml' to your project, and version >= 3.17");

            default:
                return new UtilException(e);
        }
    }
}
