package com.wk.controller;


import com.wk.pojo.User;
import com.wk.service.UserService;
import com.wk.shiro.ShiroUtil;
import com.wk.util.DataGrid;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

	@RequestMapping("list")
	@ResponseBody
	public DataGrid list(){
		List<User> list = userService.list();
		if (list != null) {
			return DataGrid.successWithData(list);
		}
		return DataGrid.failed("未找到匹配数据！");
	}


}
