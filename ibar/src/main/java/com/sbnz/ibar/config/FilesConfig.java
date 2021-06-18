package com.sbnz.ibar.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "files")
public class FilesConfig {
    private String photosPath;
    private String coverPath;
    private String pdfPath;
}
