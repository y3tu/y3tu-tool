package com.y3tu.tool.web.handler;


import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.util.CharUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.web.annotation.ClassNameMapping;
import com.y3tu.tool.web.annotation.MethodMapping;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Controller映射处理
 *
 * @author y3tu
 * @date 2018/10/27
 * @see ClassNameMapping
 * 此注解的实现类
 */
@Component
public class ClassNameMappingHandler extends RequestMappingHandlerMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        if (AnnotationUtils.findAnnotation(beanType, ClassNameMapping.class) != null || AnnotationUtils.findAnnotation(beanType, MethodMapping.class) != null) {
            return true;
        }
        return super.isHandler(beanType);
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = null;

        ClassNameMapping classNameMapping = AnnotationUtils.findAnnotation(handlerType, ClassNameMapping.class);
        MethodMapping methodMapping = AnnotationUtils.findAnnotation(method, MethodMapping.class);
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);


        if (methodMapping != null) {
            return createRequestMappingInfo(method, handlerType, methodMapping);
        } else if (classNameMapping != null) {
            return createPrimitiveRequestMappingInfo(method, handlerType, requestMapping);
        }
        return null;
    }

    protected RequestMappingInfo createRequestMappingInfo(Method method, Class<?> handlerType, MethodMapping methodAnnotation) {

        RequestMapping requestMapping = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);

        List<String> mappingStrList = CollectionUtil.newArrayList();
        String mappingStr = "";
        if (requestMapping != null) {
            String[] strArr = requestMapping.value();
            String methodValue = methodAnnotation.value();
            methodValue = handlePathStr(method.getName(), methodValue);
            for (String str : strArr) {
                mappingStrList.add(str + methodValue);
            }
        } else {
            String methodValue = methodAnnotation.value();
            methodValue = handlePathStr(method.getName(), methodValue);
            mappingStr = calcRouterStrNew(handlerType.getName() + methodValue);
            mappingStrList.add(mappingStr);
        }
        String[] mappingStrArr = ArrayUtil.toArray(mappingStrList, String.class);
        RequestMethodsRequestCondition methodMapping = new RequestMethodsRequestCondition(methodAnnotation.method());
        return new RequestMappingInfo(new PatternsRequestCondition(mappingStrArr), methodMapping, null, null, null, null, null);
    }

    protected RequestMappingInfo createPrimitiveRequestMappingInfo(Method method, Class<?> handlerType, RequestMapping requestMapping) {
        RequestMappingInfo info = this.createRequestMappingInfo(method);
        if (info != null) {
            String mappingStr = calcRouterStrNew(handlerType.getName());
            RequestMappingInfo typeInfo = new RequestMappingInfo(new PatternsRequestCondition(new String[]{mappingStr}), null, null, null, null, null, null);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        RequestCondition<?> condition = (element instanceof Class ?
                getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
        return (requestMapping != null ? createRequestMappingInfo(requestMapping, condition) : null);
    }

    protected String calcRouterStrNew(String className) {
        List<String> stringList = StrUtil.splitTrim(className, CharUtil.DOT);
        className = CollectionUtil.getLast(stringList);
        return className;
    }

    private String handlePathStr(String methodName, String methodValue) {
        if (StrUtil.isNotEmpty(methodValue)) {
            if (!StrUtil.startWith(methodValue, "/")) {
                methodValue = "/" + methodValue;
            }
            return methodValue;
        } else {
            return "/" + methodName;
        }
    }
}
