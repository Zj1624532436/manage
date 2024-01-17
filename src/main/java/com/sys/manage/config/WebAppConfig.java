package com.sys.manage.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Web 项目配置
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE","OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**","/love-time/**")
                .addResourceLocations("file:D:/project/manage/src/main/resources/img/")
                .addResourceLocations("file:D:/project/manage/src/main/resources/love-time/")
                .addResourceLocations("classpath:/img/")
                .addResourceLocations("classpath:/love-time/");// 项目启动之后要确认被加载
    }
}
