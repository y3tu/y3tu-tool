package com.y3tu.tool.web.base.jpa;

import com.y3tu.tool.web.base.mybatis.BaseMapper;
import com.y3tu.tool.web.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BaseServiceImpl<R extends JpaRepository, T> implements BaseService{

    @Autowired
    R baseRepository;

    @Override
    public Page page(Page page) {


        baseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);

    }
}
