package com.y3tu.tool.web.base.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本服务实现
 *
 * @author y3tu
 */
public class BaseServiceImpl<R extends BaseRepository, T> implements BaseService<T> {

    @Autowired
    public R repository;

    @Override
    public PageInfo page(PageInfo pageInfo) {
        //排序
        List<String> ascArr = pageInfo.getAsc();
        List<String> descArr = pageInfo.getDesc();
        List<Sort.Order> orderList = new ArrayList<>();
        if (ascArr != null) {
            for (String asc : ascArr) {
                Sort.Order order = new Sort.Order(Sort.Direction.ASC, asc);
                orderList.add(order);
            }
        }
        if (descArr != null) {
            for (String desc : descArr) {
                Sort.Order order = new Sort.Order(Sort.Direction.DESC, desc);
                orderList.add(order);
            }
        }
        //前台传入current是从1开始的，后台是从0开始的，需要减1
        PageRequest pageable = PageRequest.of(pageInfo.getCurrent() - 1, pageInfo.getSize(), Sort.by(orderList));
        Page<T> page = repository.findAll((root, criteriaQuery, criteriaBuilder)
                -> QueryHelp.getPredicate(root, pageInfo.entity, criteriaBuilder), pageable);
        pageInfo.setRecords(page.getContent());
        pageInfo.setTotal(page.getTotalElements());
        return pageInfo;
    }

    @Override
    public List<T> getAll(T criteria) {
        return repository.findAll((root, criteriaQuery, criteriaBuilder)
                -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public T getById(Object key) {
        return (T) repository.findById(key).get();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public T create(T entity) {
        return (T) repository.save(entity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public T update(T entity) {
        return (T) repository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Object[] keys) {
        for (Object id : keys) {
            repository.deleteById(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Object key) {
        repository.deleteById(key);
    }
}
