package com.y3tu.tool.core.util;

import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.io.IORuntimeException;
import com.y3tu.tool.core.io.IOUtil;
import com.y3tu.tool.core.text.CharsetUtil;
import com.y3tu.tool.core.text.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 系统运行时工具类，用于执行系统命令的工具
 *
 * @author xiaoleilu
 */
public class RuntimeUtil {

    private static AtomicInteger shutdownHookThreadIndex = new AtomicInteger(0);

    /**
     * 执行系统命令，使用系统默认编码
     *
     * @param cmds 命令列表，每个元素代表一条命令
     * @return 执行结果
     * @throws IORuntimeException IO异常
     */
    public static String execForStr(String... cmds) throws IORuntimeException {
        return execForStr(CharsetUtil.systemCharset(), cmds);
    }

    /**
     * 执行系统命令，使用系统默认编码
     *
     * @param charset 编码
     * @param cmds    命令列表，每个元素代表一条命令
     * @return 执行结果
     * @throws IORuntimeException IO异常
     */
    public static String execForStr(Charset charset, String... cmds) throws IORuntimeException {
        return getResult(exec(cmds), charset);
    }

    /**
     * 执行系统命令，使用系统默认编码
     *
     * @param cmds 命令列表，每个元素代表一条命令
     * @return 执行结果，按行区分
     * @throws IORuntimeException IO异常
     */
    public static List<String> execForLines(String... cmds) throws IORuntimeException {
        return execForLines(CharsetUtil.systemCharset(), cmds);
    }

    /**
     * 执行系统命令，使用系统默认编码
     *
     * @param charset 编码
     * @param cmds    命令列表，每个元素代表一条命令
     * @return 执行结果，按行区分
     * @throws IORuntimeException IO异常
     */
    public static List<String> execForLines(Charset charset, String... cmds) throws IORuntimeException {
        return getResultLines(exec(cmds), charset);
    }

    /**
     * 执行命令<br>
     * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
     *
     * @param cmds 命令
     * @return {@link Process}
     */
    public static Process exec(String... cmds) {
        if (ArrayUtil.isEmpty(cmds)) {
            throw new NullPointerException("Command is empty !");
        }

        // 单条命令的情况
        if (1 == cmds.length) {
            final String cmd = cmds[0];
            if (StringUtils.isBlank(cmd)) {
                throw new NullPointerException("Command is empty !");
            }
            cmds = StringUtils.split(cmd, StringUtils.C_SPACE);
        }

        Process process;
        try {
            process = new ProcessBuilder(cmds).redirectErrorStream(true).start();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return process;
    }

    /**
     * 执行命令<br>
     * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
     *
     * @param envp 环境变量参数，传入形式为key=value，null表示继承系统环境变量
     * @param cmds 命令
     * @return {@link Process}
     */
    public static Process exec(String[] envp, String... cmds) {
        return exec(envp, cmds);
    }

    /**
     * 执行命令<br>
     * 命令带参数时参数可作为其中一个参数，也可以将命令和参数组合为一个字符串传入
     *
     * @param envp 环境变量参数，传入形式为key=value，null表示继承系统环境变量
     * @param dir  执行命令所在目录（用于相对路径命令执行），null表示使用当前进程执行的目录
     * @param cmds 命令
     * @return {@link Process}
     */
    public static Process exec(String[] envp, File dir, String... cmds) {
        if (ArrayUtil.isEmpty(cmds)) {
            throw new NullPointerException("Command is empty !");
        }

        // 单条命令的情况
        if (1 == cmds.length) {
            final String cmd = cmds[0];
            if (StringUtils.isBlank(cmd)) {
                throw new NullPointerException("Command is empty !");
            }
            cmds = StringUtils.split(cmd, StringUtils.C_SPACE);
        }
        try {
            return Runtime.getRuntime().exec(cmds, envp, dir);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    // -------------------------------------------------------------------------------------------------- result

    /**
     * 获取命令执行结果，使用系统默认编码，获取后销毁进程
     *
     * @param process {@link Process} 进程
     * @return 命令执行结果列表
     */
    public static List<String> getResultLines(Process process) {
        return getResultLines(process, CharsetUtil.systemCharset());
    }

    /**
     * 获取命令执行结果，使用系统默认编码，获取后销毁进程
     *
     * @param process {@link Process} 进程
     * @param charset 编码
     * @return 命令执行结果列表
     */
    public static List<String> getResultLines(Process process, Charset charset) {
        InputStream in = null;
        try {
            in = process.getInputStream();
            return IOUtil.readLines(in, charset, new ArrayList<String>());
        } finally {
            IOUtil.close(in);
            destroy(process);
        }
    }

    /**
     * 获取命令执行结果，使用系统默认编码，，获取后销毁进程
     *
     * @param process {@link Process} 进程
     * @return 命令执行结果列表
     */
    public static String getResult(Process process) {
        return getResult(process, CharsetUtil.systemCharset());
    }

    /**
     * 获取命令执行结果，获取后销毁进程
     *
     * @param process {@link Process} 进程
     * @param charset 编码
     * @return 命令执行结果列表
     */
    public static String getResult(Process process, Charset charset) {
        InputStream in = null;
        try {
            in = process.getInputStream();
            return IOUtil.read(in, charset);
        } finally {
            IOUtil.close(in);
            destroy(process);
        }
    }

    /**
     * 销毁进程
     *
     * @param process 进程
     */
    public static void destroy(Process process) {
        if (null != process) {
            process.destroy();
        }
    }

    /**
     * 获得当前进程的PID
     * 当失败时返回-1
     */
    public static int getPid() {
        //format: "pid@hostname"
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        String[] split = jvmName.split("@");
        if (split.length != 2) {
            return -1;
        }

        try {
            return Integer.parseInt(split[0]);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 返回应用启动到现在的毫秒数
     *
     * @return
     */
    public static long getUpTime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    /**
     * 返回输入的JVM参数列表
     */
    public static String getVmArguments() {
        List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return StringUtils.join(vmArguments, " ");
    }

    /**
     * 获取CPU核数
     */
    public static int getCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 注册JVM关闭时的钩子程序
     *
     * @param runnable 钩子程序
     */
    public static void addShutdownHook(Runnable runnable) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(runnable, "Thread-ShutDownHook-" + shutdownHookThreadIndex.incrementAndGet()));
    }

    //////// 通过StackTrace 获得当前方法的调用者 ////

    /**
     * 通过StackTrace，获得调用者的类名.
     * <p>
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCallerClass() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 4) {
            StackTraceElement element = stacktrace[3];
            return element.getClassName();
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 通过StackTrace，获得调用者的"类名.方法名()"
     * <p>
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCallerMethod() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 4) {
            StackTraceElement element = stacktrace[3];
            return element.getClassName() + '.' + element.getMethodName() + "()";
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 通过StackTrace，获得当前方法的类名.
     * <p>
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCurrentClass() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 3) {
            StackTraceElement element = stacktrace[2];
            return element.getClassName();
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 通过StackTrace，获得当前方法的"类名.方法名()"
     * <p>
     * 获取StackTrace有消耗，不要滥用
     */
    public static String getCurrentMethod() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        if (stacktrace.length >= 3) {
            StackTraceElement element = stacktrace[2];
            return element.getClassName() + '.' + element.getMethodName() + "()";
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 获得运行时对象
     *
     * @return {@link Runtime}
     */
    public final Runtime getRuntime() {
        return Runtime.getRuntime();
    }

    /**
     * 获得JVM最大可用内存
     *
     * @return 最大可用内存
     */
    public final long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * 获得JVM已分配内存
     *
     * @return 已分配内存
     */
    public final long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * 获得JVM已分配内存中的剩余空间
     *
     * @return 已分配内存中的剩余空间
     */
    public final long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * 获得JVM最大可用内存
     *
     * @return 最大可用内存
     */
    public final long getUsableMemory() {
        Runtime currentRuntime = Runtime.getRuntime();
        return currentRuntime.maxMemory() - currentRuntime.totalMemory() + currentRuntime.freeMemory();
    }

}
