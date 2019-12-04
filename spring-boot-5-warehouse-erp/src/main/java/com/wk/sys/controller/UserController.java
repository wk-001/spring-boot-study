package com.wk.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.common.*;
import com.wk.sys.entity.User;
import com.wk.sys.service.UserService;
import com.wk.sys.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

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

	/**
	 * 根据ID删除用户表和用户角色表的数据
	 */
	@RequestMapping("delete")
	public ResultObj delete(Integer id){
		try {
			userService.deleteUserById(id);
			return ResultObj.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.DELETE_ERROR;
		}
	}

	/**
	 * 存在更新记录，否插入一条记录
	 */
	@RequestMapping("saveOrUpdate")
	public ResultObj saveOrUpdate(User user){
		try {
			if(user.getId()==null){
				user = PasswordUtils.encryptPassword(user);
				user.setType(Constast.USER_TYPE_NORMAL);
				user.setHiredate(new Date());
			}
			userService.saveOrUpdate(user);
			return ResultObj.OPERATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObj.OPERATE_ERROR;
		}
	}

	/**
	 * 查找最大的排序码
	 */
	@RequestMapping("getMaxOrderNum")
	public Integer getMaxOrderNum(){
		return userService.getMaxOrderNum();
	}

	/**
	 * 根据部门ID查询用户
	 */
	@RequestMapping("getUserByDeptId")
	public DataGridView getUserByDeptId(Integer deptId){
		QueryWrapper<User> wrapper = new QueryWrapper<User>()
				.eq(null!=deptId,"deptid", deptId)
				.eq("available", Constast.AVAILABLE_TRUE)
				.eq("type", Constast.USER_TYPE_NORMAL);
		List<User> list = userService.list(wrapper);
		return new DataGridView(list);
	}

	/**
	 * 中文转拼音
	 */
	@RequestMapping("changeLanguage")
	public String changeLanguage(String name){
		if(StringUtils.isNotBlank(name)){
			String pingYin = PinyinUtils.getPingYin(name);
			return pingYin;
		}else {
			return "";
		}
	}
}

