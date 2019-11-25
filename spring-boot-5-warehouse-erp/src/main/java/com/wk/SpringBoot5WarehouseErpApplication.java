package com.wk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan(basePackages = {"com.wk.sys.mapper","com.wk.bus.mapper"})
@SpringBootApplication
public class SpringBoot5WarehouseErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot5WarehouseErpApplication.class, args);
    }

}
