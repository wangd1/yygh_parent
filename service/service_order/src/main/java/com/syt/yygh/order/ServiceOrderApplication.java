package com.syt.yygh.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: wangdi
 * @Date: 2021/4/4
 */
@SpringBootApplication
@ComponentScan({"com.syt"})
@EnableFeignClients(basePackages = "com.syt")
@EnableDiscoveryClient
public class ServiceOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class,args);
    }

}
