package com.y3tu.tool.serv.report.runner;

import com.y3tu.tool.core.thread.ThreadUtil;
import com.y3tu.tool.serv.report.entity.domain.ReportDownload;
import com.y3tu.tool.serv.report.service.ReportDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 报表下载进程
 *
 * @author y3tu
 */
@Slf4j
@Component
public class ReportDownloadRunner implements ApplicationRunner {

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    ReportDownloadService reportDownloadService;

    ExecutorService executor;

    public ReportDownloadRunner() {
        ThreadFactory factory = ThreadUtil.newNamedThreadFactory("生成报表下载线程池", true);
        executor = ThreadUtil.newFixedExecutor(20, factory);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (context.isActive()) {
            //当程序启动后开启报表下载处理线程池

            //获取待处理的报表下载
            //todo  考虑分布式处理防止报表下载重处理
            List<ReportDownload> reportDownloadList = reportDownloadService.getWaitData();


        }
    }
}
