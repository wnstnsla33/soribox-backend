package pProject.pPro.securityConfig;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

//	@Override
//	public void addCorsMappings(CorsRegistry corsRegistry) {
//	    corsRegistry.addMapping("/**")
//	        .allowedOrigins("http://15.164.75.149:8080","http://soribox.kro.kr") // 실제 배포 주소
//	        .allowedMethods("*")
//	        .allowCredentials(true)
//	        .exposedHeaders("Set-Cookie");
//	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/uploads/**")
	        .addResourceLocations("file:/home/ubuntu/uploads/");
	}


}