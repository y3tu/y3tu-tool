package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.report.entity.domain.ReportParam;
import com.y3tu.tool.report.repository.ReportParamRepository;
import com.y3tu.tool.report.service.ReportParamService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author y3tu
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReportParamServiceImpl extends BaseServiceImpl<ReportParamRepository, ReportParam> implements ReportParamService {

}
