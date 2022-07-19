package com.remote.targetDetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.remote"})
@EntityScan("com.remote.models")
@EnableDiscoveryClient
@SpringBootApplication
public class targetDetection {
    public static void main(String[] args)
    {
        SpringApplication.run(targetDetection.class,args);
    }
}
