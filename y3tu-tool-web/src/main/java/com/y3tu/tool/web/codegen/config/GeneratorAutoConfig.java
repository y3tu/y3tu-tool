
package com.y3tu.tool.web.codegen.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author y3tu
 */
@Configuration
@MapperScan("com.y3tu.tool.web.codegen.mapper" )
public class GeneratorAutoConfig {


    /**
     * 分页插件
     *
     * @return PaginationInterceptor
     */
    @Bean
    @ConditionalOnMissingClass("com.baomidou.mybatisplus.plugins.PaginationInterceptor" )
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


}
