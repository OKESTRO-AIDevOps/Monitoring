/*
 * Developed by dy.choi@okestro.com on 2020-07-24
 * Last modified 2020-07-24 10:37:37
 */

package com.okestro.symphony.dashboard.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET","POST")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}
