package com.y3tu.tool.core.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 系统相关信息
 *
 * @author y3tu
 *
 * @see cn.hutool.system.SystemUtil
 */
@Slf4j
public class SystemUtil extends cn.hutool.system.SystemUtil {

    /**
     * 执行外部程序,并获取标准输出
     *
     * @param cmd      指令
     * @param encoding 编码
     * @return
     */
    public static String executeCmd(String[] cmd, String... encoding) {
        BufferedReader bufferedReader;
        InputStreamReader inputStreamReader;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedInputStream bis = new BufferedInputStream(p.getInputStream());
            if (encoding != null && encoding.length != 0) {
                //设置编码方式
                inputStreamReader = new InputStreamReader(bis, encoding[0]);
            } else {
                inputStreamReader = new InputStreamReader(bis, "utf-8");
            }

            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            bufferedReader.close();
            inputStreamReader.close();
            p.destroy();
            return sb.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

}
