package com.sbnz.ibar.config;

import com.sbnz.ibar.interceptors.PlanInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@AllArgsConstructor
public class PlanInterceptorConfig implements WebMvcConfigurer {

    private final PlanInterceptor planInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(planInterceptor)
                .addPathPatterns("/pdf/**")
                .addPathPatterns("/audio/**")
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/plans/**");
    }

}
