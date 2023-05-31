package com.example.rstaf_test_task;

import com.example.rstaf_test_task.service.FilesStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;

@SpringBootApplication
public class RStafTestTaskApplication implements
        WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>,
        CommandLineRunner {
    @Resource
    FilesStorageService filesStorageService;

    public static void main(String[] args) {
        SpringApplication.run(RStafTestTaskApplication.class, args);
    }


    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.setPort(8080);
        //factory.setContextPath("/myapp");
    }


    @Override
    public void run(String... args) throws Exception {
        filesStorageService.init();
    }
}
