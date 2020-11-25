package com.y3tu.tool.report.repository;

import com.y3tu.tool.report.base.PageInfo;
import com.y3tu.tool.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;


/**
 * @author y3tu
 */
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    Report findById(long id);

    @Query(value = "select * from report where code=:#{#params.code} ",
            countQuery = "select count(*) from report where code=:#{#params.code}",nativeQuery = true)
    Page<Report> page(@Param("params") Map params, Pageable pageable);
}
