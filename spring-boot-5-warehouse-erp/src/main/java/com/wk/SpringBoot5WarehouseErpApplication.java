package com.wk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching     //开启基于注解的缓存
@MapperScan(basePackages = {"com.wk.*.mapper"})
@SpringBootApplication
public class SpringBoot5WarehouseErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot5WarehouseErpApplication.class, args);
    }

}
