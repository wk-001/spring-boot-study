package com.wk;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

/**
 * https://blog.csdn.net/qq_34021712/article/details/84571067
 *
 * shiro整合Redis
 * https://blog.csdn.net/qq_34021712/article/details/80791339
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBoot4ShiroApplicationTests {

	MockMvc mockMvc;        //虚拟请求

	@Autowired
	private WebApplicationContext context;

	//初始化虚拟请求
	@Before
	public void initMock(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void contextLoads() throws Exception {
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("userName","admin");
		param.add("password","123");

		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
						.params(param))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}

	//获取加密盐和加密后的密码
    @Test
    public void getPwd(){
	    String password = "1";
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("MD5", password, salt, times).toString();
        System.out.println("encodedPassword = " + encodedPassword);
        System.out.println("salt = " + salt);
    }

}
