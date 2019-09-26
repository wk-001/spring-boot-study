package com.wk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wk.pojo.User;
import com.wk.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

/**
 * SpringBoot单元测试
 * 参考页面：https://mrbird.cc/Spring-Boot%20TESTing.html
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBoot3JunitApplicationTests {

    MockMvc mockMvc;        //虚拟请求

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;        //jackson对象

    @Autowired
    private UserService userService;

    //初始化虚拟请求
    @Before
    public void initMock(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        /*模拟session
        MockHttpSession session = new MockHttpSession();
        User user =new User();
        user.setName("tom");
        user.setAge(22);
        session.setAttribute("user", user);*/
    }

    //发送restful风格的请求
    @Test
    public void contextLoads() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())   //显示请求执行结果代码，如200 404
                .andDo(MockMvcResultHandlers.print());      //打印请求和响应信息
    }

    //发送json格式数据
    @Test
    public void testJson() throws Exception {
        User user = new User(1,"tom",22);
        String userJson = mapper.writeValueAsString(user);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/user/testJson")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)   //设置请求头为json格式
                        .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    //检查返回JSON数据中某个值的内容,需要返回json
    @Test
    public void testJosnReturn() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/user/{id}", "2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tom"))
                .andDo(MockMvcResultHandlers.print());
    }

    //比较model，运行该方法需要跳转页面，不能使用@RestController、@ResponseBody注解
    @Test
    public void testMode() throws Exception {
        /*比较model*/
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("id","1");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/user/getById")
                        .params(param))
                //判断ModelAndView向前台传参个数
                .andExpect(MockMvcResultMatchers.model().size(2))
                //判断ModelAndView的参数中是否有指定的key
                .andExpect(MockMvcResultMatchers.model().attributeExists("name"))
                //判断ModelAndView的参数中是否有指定的key 和value
                .andExpect(MockMvcResultMatchers.model().attribute("age", "22"))
                /*判断controller方法是否返回某视图*/
                .andExpect(MockMvcResultMatchers.view().name("index.html"))
                .andDo(MockMvcResultHandlers.print());
    }

    //比较返回内容
    @Test
    public void testReturn() throws Exception {
        /* 返回内容为hello
        mockMvc.perform(MockMvcRequestBuilders.get("/user/testReturn"))
                .andExpect(MockMvcResultMatchers.content().string("hello1"))
                .andDo(MockMvcResultHandlers.print());  //输出响应结果*/

        /* 返回内容是XML，并且与xmlCotent一样
        mockMvc.perform(MockMvcRequestBuilders.get("/index"))
                .andExpect(MockMvcResultMatchers.content().xml(xmlContent));*/

        /* 返回内容是JSON ，并且与jsonContent一样*/

        String jsonContent = "{\"id\":1,\"name\":\"tom\",\"age\":22}";
        mockMvc.perform(MockMvcRequestBuilders.get("/user/testReturn"))
                .andExpect(MockMvcResultMatchers.content().json(jsonContent))
                .andDo(MockMvcResultHandlers.print());

    }

    //使用assert断言测试service方法是否符合预期返回值
    @Test
    public void testService(){
        User user = userService.getById(2);
        Assert.assertEquals("用户名是tom","tom",user.getName());
    }

    //测试方法加上@Transactional注解可以回滚，不会污染数据库
    @Test
    @Transactional
    public void testDel(){
        userService.removeById(4);
    }

    //模拟session和cookie
    @Test
    public void testSessionAndCookie() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/index").sessionAttr("user", new User()));
        mockMvc.perform(MockMvcRequestBuilders.get("/index").cookie(new Cookie("user", "abc")));
    }
}
