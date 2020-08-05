package com.krasimirkolchev.photomag.config;

import com.krasimirkolchev.photomag.web.interceptors.LoggerInterceptor;
import com.krasimirkolchev.photomag.web.interceptors.PageTitleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationWebMVCConfiguration implements WebMvcConfigurer {
    private final PageTitleInterceptor pageTitleInterceptor;
    private final LoggerInterceptor loggerInterceptor;

    @Autowired
    public ApplicationWebMVCConfiguration(PageTitleInterceptor pageTitleInterceptor, LoggerInterceptor loggerInterceptor) {
        this.pageTitleInterceptor = pageTitleInterceptor;
        this.loggerInterceptor = loggerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageTitleInterceptor);
        registry.addInterceptor(loggerInterceptor);
    }
}
