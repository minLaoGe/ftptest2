package com.example.ftptest2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync//开启异步调用
public class Ftptest2Application {
    public static ConfigurableApplicationContext ac;

    public static void main(String[] args) {
        ac= SpringApplication.run(Ftptest2Application.class, args);
    }

}
