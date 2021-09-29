package com.fabbi.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fabbi.constant.Constant;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path productUploadDir = Paths.get(Constant.UPLOAD_DIR);
		log.info("------------------------- Upload_Dir: [" + productUploadDir + "]");
		
		String productUploadPath = productUploadDir.toFile().getAbsolutePath();
		log.info("------------------------- Upload_Path: [" + productUploadPath + "]");
		
		registry.addResourceHandler("/product-images/**").addResourceLocations("file:/" + productUploadPath + "/");
	}
}
