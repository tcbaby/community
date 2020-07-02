package com.tcbaby.community.config;

import com.tcbaby.community.web.intercepter.AuthInterceptor;
import com.tcbaby.community.web.intercepter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tcbaby
 * @date 20/05/06 9:39
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/**/*.js", "/**/*.css", "/**/*.html", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/img/*", "/**/login", "/**/captcha");

        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/**/*.js", "/**/*.css", "/**/*.html", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/img/*", "/**/captcha");
    }
}
