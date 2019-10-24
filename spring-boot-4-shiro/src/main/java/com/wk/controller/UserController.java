package com.wk.controller;


import com.wk.pojo.User;
import com.wk.service.UserService;
import com.wk.shiro.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-09-27
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	//该用户具有"user:list"的权限才能执行此方法
	@RequiresPermissions("user:list")
	@RequestMapping("list")
	public String userList(Model model){
		model.addAttribute("msg","获取用户信息");
		return "user/list";
	}

	@RequiresPermissions("user:update")
	@RequestMapping("update")
	public String userUpdate(Model model){
		model.addAttribute("msg","修改用户信息");
		return "user/list";
	}

	//该用户具有"admin"的角色才能执行此方法
	@RequiresRoles("admin")
	@RequestMapping("del")
	public String userDel(Model model){
		model.addAttribute("msg","删除用户信息");
		return "user/list";
	}

	@RequestMapping("checkPassword")
	@ResponseBody
	public int checkPassword(String oldPwd,String newPwd){
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User) session.getAttribute("user");
		Integer id = user.getId();
		String salt = user.getSalt();
		int times = 2;
		String encodedPassword = new SimpleHash("MD5", oldPwd, salt, times).toString();
		User userByDb = userService.getById(id);
		if(userByDb.getPassword().equals(encodedPassword)){
			user.setPassword(newPwd);
			User user1 = ShiroUtil.encryptPassword(user);
			userService.updateById(user1);
			return 1;
		}else {
			return 2;
		}
	}

}
