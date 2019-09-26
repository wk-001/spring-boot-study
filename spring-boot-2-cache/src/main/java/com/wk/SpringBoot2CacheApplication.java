package com.wk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching			//开启缓存功能
@SpringBootApplication
@MapperScan("com.wk.dao")
public class SpringBoot2CacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot2CacheApplication.class, args);
	}

}
