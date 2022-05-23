package com.milosh.lab04.config;

import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Component
public class WebMvcConfigurerImpl implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error403").setViewName("error403");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        //globalna obsługa daty w formularzach
        registry.addFormatter(new DateFormatter("yyyy-MM-dd"));
    }

}
