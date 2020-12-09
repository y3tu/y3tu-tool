package com.y3tu.tool.web.base.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 基础Repository
 *
 * @author y3tu
 */
public interface BaseRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}
