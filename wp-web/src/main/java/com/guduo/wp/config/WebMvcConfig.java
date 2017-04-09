package com.guduo.wp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by zhangpeng on 16-4-12.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
	

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/").setViewName("view/index");
        registry.addViewController("/login").setViewName("view/login");
        registry.addViewController("/register").setViewName("view/register");
        registry.addViewController("/user_info").setViewName("view/register_next");
        registry.addViewController("/register_finish").setViewName("view/register_finish");
        registry.addViewController("/modify_account").setViewName("view/modify_account");
        registry.addViewController("/reset_password").setViewName("view/reset_password");
        registry.addViewController("/pay").setViewName("view/pay");
        registry.addViewController("/pay_finish").setViewName("view/pay_finish");
        registry.addViewController("/detail").setViewName("view/detail");
        registry.addViewController("/week_report").setViewName("view/week_report");
        registry.addViewController("/month_report").setViewName("view/month_report");
        registry.addViewController("/record_list").setViewName("view/record_list");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/user/**").addResourceLocations("classpath:/static/")
                .resourceChain(false)
        ;
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
        .resourceChain(false);
        
        registry.addResourceHandler("/doc/**").addResourceLocations("classpath:/static/")
        .resourceChain(false)
		;
		registry.addResourceHandler("/doc/**").addResourceLocations("classpath:/static/")
		.resourceChain(false);
    }


}
