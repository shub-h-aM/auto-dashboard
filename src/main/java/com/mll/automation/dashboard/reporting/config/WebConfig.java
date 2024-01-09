package com.mll.automation.dashboard.reporting.config;

import com.mll.automation.dashboard.reporting.convertor.DateConvertor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public DateConvertor dateConvertor() {
        return new DateConvertor();
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(dateConvertor());
    }
}
