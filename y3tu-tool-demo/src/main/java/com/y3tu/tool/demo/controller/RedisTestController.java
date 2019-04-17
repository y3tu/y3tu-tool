package com.y3tu.tool.demo.controller;

import com.y3tu.tool.cache.redis.template.RedisRepository;
import com.y3tu.tool.core.pojo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Redis测试
 * @author y3tu
 * @date 2019-04-08
 */
@RestController
@RequestMapping("redisTest")
public class RedisTestController {

//    @Autowired
//    RedisRepository redisRepository;
//
//    @RequestMapping(value = "/setValue",method = RequestMethod.GET)
//    R setValue(@RequestParam String key,@RequestParam String value){
//        redisRepository.set(key,value);
//        return R.success();
//    }
//
//    @RequestMapping(value = "/getValue",method = RequestMethod.GET)
//    R getValue(@RequestParam String key){
//        String resultStr = redisRepository.get(key);
//        return R.success(resultStr);
//    }


}
