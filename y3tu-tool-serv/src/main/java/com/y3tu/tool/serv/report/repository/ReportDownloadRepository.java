package com.y3tu.tool.serv.report.repository;

import com.y3tu.tool.serv.report.entity.domain.ReportDownload;
import com.y3tu.tool.web.base.jpa.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author y3tu
 */
public interface ReportDownloadRepository extends BaseRepository<ReportDownload> {
    /**
     * 根据reportId获取有效的和正在生成的报表下载记录
     *
     * @param reportId
     * @param status
     * @return
     */
    List<ReportDownload> getByReportIdAndStatusIsIn(int reportId, List<String> status);

    /**
     * 根据报表名称查询报表下载信息
     *
     * @param reportName
     * @param pageable
     * @return
     */
    @Query(value = "select a.id," +
            "a.report_id as reportId, " +
            "a.status," +
            "a.download_url as downloadUrl," +
            "a.download_times as downloadTimes," +
            "a.param_json as paramJson," +
            "a.err_msg as errMsg, " +
            "b.name as reportName from report_download a,report b where if(:reportName !='', b.name like %:reportName%,1=1)  ", nativeQuery = true)
    Page<List<Map<String, Object>>> getReportDownloadByPage(@Param("reportName") String reportName, Pageable pageable);


    /**
     * 根据状态获取报表下载数据
     *
     * @return
     */
    @Query(value="select * from report_download where status = '00W' ",nativeQuery = true)
    List<ReportDownload> getWaitData();
}

