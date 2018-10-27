package com.y3tu.tool.web.handler;

import com.y3tu.tool.web.annotation.MethodMapping;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author y3tu
 * @date 2018/10/27
 * @see MethodMapping 注解的实现
 */
@Component
@Lazy
public class MethodMappingHandler extends WebMvcConfigurationSupport {

    @Override
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = super.requestMappingHandlerAdapter();
        List<HttpMessageConverter<?>> converters = super.getMessageConverters();
        List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
        argumentResolvers.add(new MethodMappinngProcessor(converters));
        requestMappingHandlerAdapter.setCustomArgumentResolvers(argumentResolvers);
        return requestMappingHandlerAdapter;
    }

    class MethodMappinngProcessor extends RequestResponseBodyMethodProcessor {

        public MethodMappinngProcessor(List<HttpMessageConverter<?>> converters) {
            super(converters);
        }

        public MethodMappinngProcessor(List<HttpMessageConverter<?>> messageConverters, ContentNegotiationManager contentNegotiationManager) {
            super(messageConverters, contentNegotiationManager);
        }

        public MethodMappinngProcessor(List<HttpMessageConverter<?>> messageConverters, ContentNegotiationManager contentNegotiationManager, List<Object> responseBodyAdvice) {
            super(messageConverters, contentNegotiationManager, responseBodyAdvice);
        }

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            if (parameter.getMethodAnnotation(MethodMapping.class) != null) {
                return true;
            }
            return super.supportsParameter(parameter);
        }

        @Override
        public boolean supportsReturnType(MethodParameter returnType) {
            return super.supportsReturnType(returnType);
        }
    }

}
