package com.y3tu.tool.lowcode.monitor.jvm;

import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.util.SystemUtil;
import com.y3tu.tool.lowcode.monitor.entity.JstatDto;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


/**
 * 性能分析数据
 *
 * @author y3tu
 */
public class Jstat {

    /**
     * 性能分析
     *
     * @param args 执行命令
     * @return
     */
    public static List<JstatDto> jstat(String[] args) {
        try {
            List<JstatDto> jstatList = new ArrayList<>();
            String cmdResult = SystemUtil.executeCmd(args);
            BufferedReader reader = new BufferedReader(new StringReader(cmdResult));
            String[] keys = ArrayUtil.removeEmpty(reader.readLine().split("\\s+|\t"));
            String[] values = ArrayUtil.removeEmpty(reader.readLine().split("\\s+|\t"));
            // 特殊情况
            if ("-compiler".equals(args[1])) {
                for (int i = 0; i < 4; i++) {
                    jstatList.add(new JstatDto(keys[i], values[i]));
                }
                return jstatList;
            }
            // 正常流程
            for (int i = 0; i < keys.length; i++) {
                jstatList.add(new JstatDto(keys[i], values[i]));
            }
            return jstatList;
        } catch (Exception e) {
            throw new ToolException("获取性能分析数据异常！" + e.getMessage(), e);
        }
    }

    /**
     * 类加载信息
     *
     * @return
     */
    public static List<JstatDto> jstatClass() {
        String pid = String.valueOf(SystemUtil.getCurrentPID());
        List<JstatDto> jstatClass = jstat(new String[]{"jstat", "-class", pid});
        List<JstatDto> jstatCompiler = jstat(new String[]{"jstat", "-compiler", pid});
        jstatClass.addAll(jstatCompiler);
        return jstatClass;
    }

    /**
     * 堆内存信息
     *
     * @return
     */
    public static List<JstatDto> jstatGc() {
        String pid = String.valueOf(SystemUtil.getCurrentPID());
        return jstat(new String[]{"jstat", "-gc", pid});
    }

    /**
     * 堆内存百分比
     *
     * @return
     */
    public static List<JstatDto> jstatUtil() {
        String pid = String.valueOf(SystemUtil.getCurrentPID());
        return jstat(new String[]{"jstat", "-gcutil", pid});
    }


}
