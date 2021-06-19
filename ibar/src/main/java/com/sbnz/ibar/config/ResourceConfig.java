package com.sbnz.ibar.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class ResourceConfig implements WebMvcConfigurer {

    private final FilesConfig filesConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photos/**")
                .addResourceLocations(String.format("file:%s", filesConfig.getPhotosPath()));
        registry.addResourceHandler("/covers/**")
                .addResourceLocations(String.format("file:%s", filesConfig.getCoverPath()));
        registry.addResourceHandler("/pdf/**")
                .addResourceLocations(String.format("file:%s", filesConfig.getPdfPath()));
    }
}