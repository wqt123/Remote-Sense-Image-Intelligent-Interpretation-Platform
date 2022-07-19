package com.remote.terrianClassification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.remote"})
@EnableDiscoveryClient
@EntityScan("com.remote.models")
@SpringBootApplication
public class terrianClassfication {
    public static void main(String[] args)
    {
        SpringApplication.run(terrianClassfication.class,args);
    }
}
