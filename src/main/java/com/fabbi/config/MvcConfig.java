package com.fabbi.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fabbi.constant.Constant;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path productUploadDir = Paths.get(Constant.UPLOAD_DIR);
		String productUploadPath = productUploadDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/product-images/**").addResourceLocations("file:/" + productUploadPath + "/");
	}
}
