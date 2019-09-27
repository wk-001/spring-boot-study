package com.wk.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wk.pojo.User;
import com.wk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-09-24
 */
@RestController
//@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;        //jackson对象

    @GetMapping("{id}")
    public User getById(@PathVariable int id){
        return userService.getById(id);
    }

    @RequestMapping("getById")
    public ModelAndView getUserById(int id){
        User user = userService.getById(id);
        ModelAndView mav = new ModelAndView("index.html");
        mav.addObject("name",user.getName());
        mav.addObject("age","22");
        return mav;
    }

    @PostMapping("testJson")
    public User testJson(@RequestBody User user){
        return user;
    }

    @RequestMapping("testReturn")
    @ResponseBody
    public String testReturn() throws JsonProcessingException {
        User user = new User(1,"tom",22);
        String userJson = mapper.writeValueAsString(user);
        return userJson;
        //        return "hello";
    }
}
