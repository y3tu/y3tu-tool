package com.y3tu.tool.web.annotation;

import com.y3tu.tool.web.aspect.ControllerAop;
import com.y3tu.tool.web.handler.ClassNameMappingHandler;
import com.y3tu.tool.web.handler.MethodMappingHandler;
import com.y3tu.tool.web.util.SpringContextUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 添加此注解使用web工具类
 *
 * @author y3tu
 * @date 2018/10/5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({SpringContextUtil.class, ControllerAop.class, ClassNameMappingHandler.class, MethodMappingHandler.class})
public @interface EnableToolWeb {
}
