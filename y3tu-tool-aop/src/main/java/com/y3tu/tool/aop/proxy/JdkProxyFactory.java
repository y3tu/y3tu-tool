package com.y3tu.tool.aop.proxy;


import com.y3tu.tool.aop.ProxyUtil;
import com.y3tu.tool.aop.aspects.Aspect;
import com.y3tu.tool.aop.interceptor.JdkInterceptor;

/**
 * JDK实现的切面代理
 *
 * @author looly
 */
public class JdkProxyFactory extends ProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T proxy(T target, Aspect aspect) {
        return (T) ProxyUtil.newProxyInstance(target.getClass().getClassLoader(), new JdkInterceptor(target, aspect), target.getClass().getInterfaces());
    }
}
