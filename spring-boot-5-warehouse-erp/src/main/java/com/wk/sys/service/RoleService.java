package com.wk.sys.service;

import com.wk.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
public interface RoleService extends IService<Role> {

	/**
	 * 根据角色ID删除角色、用户角色、角色权限的对应数据
	 */
	void removeRoleById(Integer id);
}
