package com.wk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.wk.dao")
@SpringBootApplication
public class SpringBoot4ShiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot4ShiroApplication.class, args);
	}

}
