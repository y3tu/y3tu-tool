package com.y3tu.tool.demo.controller;

import com.y3tu.tool.demo.entity.Person;
import com.y3tu.tool.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 多级缓存测试
 * @author y3tu
 * @date 2019-04-08
 */
@RestController
@RequestMapping("/cacheTest")
public class CacheTestController {

    @Autowired
    PersonService personService;

    @RequestMapping(value = "/put",method = RequestMethod.GET)
    public long put() {
        Person p = new Person(1,"y3tu-put",30,"test");
        Person person = personService.save(p);
        return person.getId();
    }

    @RequestMapping("/able")
    public Person cacheable() {
        Person p = new Person();
        p.setId(1);
        return personService.findOne(p);
    }

    @RequestMapping("/evit")
    public String evit(@RequestBody Person person) {

        personService.remove(person.getId());
        return "ok";
    }
}
