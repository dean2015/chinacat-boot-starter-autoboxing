package com.chinacat.boot.starter.autoboxing.config;

import com.chinacat.boot.starter.autoboxing.filter.NullHandleFilter;
import com.chinacat.boot.starter.autoboxing.response.ResponseAdvisor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinacat
 */
@Configuration
public class AutoboxingConfig {

    @Bean
    public ResponseAdvisor getResponseAdvisor() {
        return new ResponseAdvisor();
    }

    @Bean
    public NullHandleFilter nullHandleFilter() {
        return new NullHandleFilter();
    }

    @Bean
    public FilterRegistrationBean principalFilterRegistration() {
        FilterRegistrationBean<NullHandleFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(nullHandleFilter());
        registration.addUrlPatterns("/*");
        registration.setName("nullHandleFilter");
        registration.setOrder(12);
        return registration;
    }
}