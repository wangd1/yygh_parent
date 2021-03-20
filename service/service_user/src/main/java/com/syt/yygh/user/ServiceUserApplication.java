package com.syt.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: wangdi
 * @Date: 2021/3/20
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.syt")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.syt")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
