package com.y3tu.tool.report.repository;

import com.y3tu.tool.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author y3tu
 */
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {
}
