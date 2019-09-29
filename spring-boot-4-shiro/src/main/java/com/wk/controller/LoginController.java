package com.wk.controller;

import com.wk.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

	//访问首页
	@RequestMapping("/")
	public String redirectIndex() {
		/*Subject subject = SecurityUtils.getSubject();
		User user=(User) subject.getPrincipal();
		if (user == null){
			return "redirect:/login";
		}else{
			return "redirect:/index";
		}*/
		return "redirect:/index";
	}

	//未登录的请求跳转到login.html页面
	@GetMapping("toLogin")
	public String toLogin() {
		return "login";
	}

	//登录
	@PostMapping("login")
	@ResponseBody
	public Map<String,Object> login(String userName, String password, Boolean rememberMe,HttpServletRequest req){
		UsernamePasswordToken token = new UsernamePasswordToken(userName,password,rememberMe);
		Subject subject = SecurityUtils.getSubject();
		String msg = "";
		try {
			subject.login(token);
			//登录成功后将用户信息放入session
			User user = (User) subject.getPrincipal();
			req.getSession().setAttribute("user",user);
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			msg = "密码错误!";
		} catch(ExcessiveAttemptsException e){
			e.printStackTrace();
			msg = "密码输入错误次数超过3次,请1小时后再试！";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			msg = "认证失败！原因："+e.getMessage();
		}

		//获取登录前访问的页面
		SavedRequest request = WebUtils.getSavedRequest(req);
		String path = "";

		if (request != null) {
			String uri = request.getRequestURI();
			if(uri.equals("/favicon.ico")||"".equals(uri)){
				path = "/index";
			}else{
				path = uri;
			}
		}

		Map<String,Object> map = new HashMap<>();
		map.put("msg",msg);
		map.put("path",path);
		return map;
	}

/*	@RequestMapping("/index")
	public String index(Model model) {
		// 登录成后，即可通过Subject获取登录的用户信息,从shiro的session中取出我们保存的对象，该对象在登录认证成功后保存的
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("user", user);
		return "index";
	}*/

}
