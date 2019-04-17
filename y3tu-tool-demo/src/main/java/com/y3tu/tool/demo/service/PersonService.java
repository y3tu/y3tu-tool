package com.y3tu.tool.demo.service;

import com.y3tu.tool.demo.entity.Person;

/**
 * @author y3tu
 * @date 2019-04-08
 */
public interface PersonService {

    Person save(Person person);

    void remove(Long id);

    void removeAll();

    Person findOne(Person person);

}
