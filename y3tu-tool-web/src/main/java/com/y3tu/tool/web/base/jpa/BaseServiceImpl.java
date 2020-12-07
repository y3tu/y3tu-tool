package com.y3tu.tool.web.base.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;


public class BaseServiceImpl<R extends JpaRepository, T> implements BaseService {

    @Autowired
    R baseRepository;

    @Override
    public Page page(Page page) {
        return null;
    }
}
