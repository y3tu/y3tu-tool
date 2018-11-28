package com.y3tu.tool.web.annotation;

import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

/**
 * 该注解用于Controller类上，把类名映射为Controller的请求路径
 * eg:
 * 调用MusicController类的getAll()方法.
 * 使用ClassNameMapping注解类，使用MethodMapping注解方法
 * 映射给前端的地址为
 * /MusicController/getAll
 * <p>
 * 如果使用@PathVariable 这类路径参数的目前只能使用原方式 @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
 *
 * @author y3tu
 * @date 2018/10/27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Controller
public @interface ClassNameMapping {
}
