package com.y3tu.tool.web.base.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 基本服务实现
 *
 * @author y3tu
 */
public class BaseServiceImpl<R extends BaseRepository, T> implements BaseService<T> {

    @Autowired
    R repository;

    @Override
    public PageInfo page(PageInfo pageInfo) {
        PageRequest pageable = PageRequest.of(pageInfo.getCurrent(), pageInfo.getSize());
        Page<T> page = repository.findAll((root, criteriaQuery, criteriaBuilder)
                -> QueryHelp.getPredicate(root, pageInfo.entity, criteriaBuilder), pageable);
        pageInfo.setRecords(page.getContent());
        pageInfo.setTotal(page.getTotalElements());
        return pageInfo;
    }

    @Override
    public List<T> queryAll(T criteria) {
        return repository.findAll((root, criteriaQuery, criteriaBuilder)
                -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public Object findById(Object key) {
        return repository.findById(key).get();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(T entity) {
        repository.save(entity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(T entity) {
        repository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Object> keys) {
        for (Object id : keys) {
            repository.deleteById(id);
        }
    }
}
