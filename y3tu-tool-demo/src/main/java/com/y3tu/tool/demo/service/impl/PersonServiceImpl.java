package com.y3tu.tool.demo.service.impl;

import com.y3tu.tool.cache.annotation.*;
import com.y3tu.tool.cache.enums.CacheMode;
import com.y3tu.tool.demo.entity.Person;
import com.y3tu.tool.demo.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author y3tu
 * @date 2019-04-08
 */
@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

    @Override
    @CachePut(value = "people", key = "#person.id", depict = "用户信息缓存",cacheMode = CacheMode.ONLY_FIRST)
    public Person save(Person person) {
        log.info("为id、key为:" + person.getId() + "数据做了缓存");
        return person;
    }

    @Override
    @CacheEvict(value = "people", key = "#id")
    public void remove(Long id) {
        log.info("删除了id、key为" + id + "的数据缓存");
        //这里不做实际删除操作
    }

    @Override
    @CacheEvict(value = "people", allEntries = true)
    public void removeAll() {
        log.info("删除了所有缓存的数据缓存");
        //这里不做实际删除操作
    }

    @Override
    @Cacheable(value = "people", key = "#person.id", depict = "用户信息缓存",cacheMode = CacheMode.ONLY_FIRST)
    public Person findOne(Person person) {
        log.info("为id、key为:" + person.getId() + "数据做了缓存");
        return person;
    }
}
