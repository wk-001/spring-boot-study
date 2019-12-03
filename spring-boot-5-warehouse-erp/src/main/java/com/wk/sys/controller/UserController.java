package com.wk.sys.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.common.DataGridView;
import com.wk.sys.entity.User;
import com.wk.sys.service.UserService;
import com.wk.sys.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("list")
	public DataGridView list(UserVo userVo){
		Page<User> page = new Page<>(userVo.getPage(), userVo.getLimit());
		//根据用户名or登录名、地址、部门ID查询除超级管理员的其他用户
		userService.queryList(page,userVo);
		return new DataGridView(page.getTotal(),page.getRecords());
	}
}

