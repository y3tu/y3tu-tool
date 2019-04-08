package com.y3tu.tool.ui.test.controller;

import com.y3tu.tool.ui.test.entity.Person;
import com.y3tu.tool.ui.test.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 多级缓存测试
 * @author y3tu
 * @date 2019-04-08
 */
@RestController
@RequestMapping("cacheTest")
public class CacheTestController {

    @Autowired
    PersonService personService;

    @RequestMapping("/put")
    public long put(@RequestBody Person person) {
        Person p = personService.save(person);
        return p.getId();
    }

    @RequestMapping("/able")
    public Person cacheable(@RequestBody Person person) {

        return personService.findOne(person);
    }

    @RequestMapping("/evit")
    public String evit(@RequestBody Person person) {

        personService.remove(person.getId());
        return "ok";
    }
}
