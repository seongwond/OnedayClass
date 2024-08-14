package com.springboot.AoooA.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private LayoutInterceptor layoutInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(layoutInterceptor).addPathPatterns("/**"); // 모든 요청에 대해 인터셉터가 동작하도록 설정합니다.
	}
	
	  @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		  CacheControl cacheControl =
				  CacheControl.noCache().mustRevalidate().cachePrivate().sMaxAge(Duration.ZERO);
	        registry.addResourceHandler("/img/**")
	                .addResourceLocations("classpath:static/img/")
	                .setCachePeriod(0)
	                .setCacheControl(cacheControl);// 캐시 기간을 설정 (초 단위)
	        
	                
	    }
}