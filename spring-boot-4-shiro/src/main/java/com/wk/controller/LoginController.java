package com.wk.controller;

import com.wk.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

	//首页
	@RequestMapping("/")
	public String redirectIndex() {
		return "redirect:/index";
	}

	@RequestMapping("login")
	@ResponseBody
	public Map<String,Object> login(String userName, String password){
		UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
		Subject subject = SecurityUtils.getSubject();
		String msg = "";
		try {
			subject.login(token);
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			System.out.println("密码"+e.getMessage());
			msg = "密码错误!"+e.getMessage();
		} catch(ExcessiveAttemptsException e){
			e.printStackTrace();
			msg = "密码输入错误次数超过3次,请1小时后再试！";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			msg = "认证失败！原因："+e.getMessage();
		}
		Map<String,Object> map = new HashMap<>();
		map.put("msg",msg);
		return map;
	}

	@RequestMapping("/index")
	public String index(Model model) {
		// 登录成后，即可通过Subject获取登录的用户信息
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("user", user);
		return "index";
	}
}
