package com.wk;

import com.wk.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * SpringBoot整合缓存和Redis
 * 参考页面：https://mrbird.cc/Spring-Boot%20cache.html
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBoot2CacheApplicationTests {

	@Autowired
	private UserService userService;


	@Test
	@Transactional
	public void contextLoads() {
		for (int i = 1; i < 6; i++) {
			userService.getById(i);
		}
		/*User user = new User();
		user.setId(6);
		user.setUserName("black");
		user.setAge(27);
		user.setEmail("black@aa.com");
		userService.updUser(user);*/
//		userService.delUser(5);
	}

}
