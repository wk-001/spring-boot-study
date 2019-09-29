package com.wk.service;

import com.wk.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author wk
 * @since 2019-09-29
 */
public interface RoleService extends IService<Role> {

	/**
	 * 根据认证成功的userId查询该用户的所有角色
	 * @param userId
	 * @return
	 */
	List<Role> getAllRolesByUserId(int userId);
}
