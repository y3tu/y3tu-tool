package com.y3tu.tool.lowcode.report.runner;

import com.y3tu.tool.core.thread.ThreadUtil;
import com.y3tu.tool.lowcode.report.entity.domain.ReportDownload;
import com.y3tu.tool.lowcode.report.service.ReportDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    /**
     * 带有定时任务的线程池
     */
    ScheduledExecutorService executor;

    public ReportDownloadRunner() {
        executor = ThreadUtil.newScheduledExecutor(1, ThreadUtil.newNamedThreadFactory("生成报表下载线程池", true));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (context.isActive()) {
            //当程序启动后开启报表下载处理线程池 初始时间间隔是1分
            executor.scheduleAtFixedRate(()->{
                //获取待处理的报表下载
                //目前采用sql for update的方式，后续考虑使用分布式锁处理防止报表下载重处理
                try {
                    List<ReportDownload> reportDownloadList = reportDownloadService.getWaitData();
                    if (!reportDownloadList.isEmpty()) {
                        for (ReportDownload reportDownload : reportDownloadList) {
                            reportDownload.setStatus(ReportDownload.STATUS_BUILDING);
                            reportDownload.setUpdateTime(new Date());
                            reportDownloadService.update(reportDownload);
                            reportDownloadService.handleDownload(reportDownload);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            },1, 1, TimeUnit.MINUTES);

        }
    }

}
