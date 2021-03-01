package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.report.entity.domain.GenerationLog;
import com.y3tu.tool.report.repository.GenerationLogRepository;
import com.y3tu.tool.report.service.GenerationLogService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author y3tu
 */
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Service
public class GenerationLogServiceImpl extends BaseServiceImpl<GenerationLogRepository, GenerationLog> implements GenerationLogService {
}
