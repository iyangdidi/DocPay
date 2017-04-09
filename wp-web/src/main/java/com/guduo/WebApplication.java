package com.guduo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
@Controller
public class WebApplication {
	
	@RequestMapping("/")
    public String greeting() {
        return "view/index";
    }

    public static void main(String[] args) {
        SpringApplication
                .run(WebApplication.class, args);
    }
}
