package com.wk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.wk.dao")
@SpringBootApplication
public class SpringBoot3JunitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot3JunitApplication.class, args);
    }

}
