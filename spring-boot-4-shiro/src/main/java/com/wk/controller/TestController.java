package com.wk.controller;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-09-27
 */
@Controller
@RequestMapping("/test")
public class TestController {

	//该用户具有"user:list"的权限才能执行此方法
	@RequiresPermissions("user:list")
	@RequestMapping("list")
	public String userList(Model model){
		model.addAttribute("msg","获取用户信息");
		return "test/test";
	}

	@RequiresPermissions("user:update")
	@RequestMapping("update")
	public String userUpdate(Model model){
		model.addAttribute("msg","修改用户信息");
		return "test/test";
	}

	//该用户具有"admin"的角色才能执行此方法
	@RequiresRoles("admin")
	@RequestMapping("del")
	public String userDel(Model model){
		model.addAttribute("msg","删除用户信息");
		return "test/test";
	}

}
