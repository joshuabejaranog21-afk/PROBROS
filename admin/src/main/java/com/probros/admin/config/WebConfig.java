package com.probros.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${site.path}")
    private String sitePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sirve las imágenes del sitio para preview en el admin
        registry.addResourceHandler("/site-images/**")
                .addResourceLocations("file:" + sitePath + "/assets/images/");
    }
}
