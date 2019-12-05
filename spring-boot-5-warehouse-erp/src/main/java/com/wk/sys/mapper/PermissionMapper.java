package com.wk.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wk.sys.entity.Permission;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
public interface PermissionMapper extends BaseMapper<Permission> {

	Integer getMaxOrderNum();

	void deleteRolePermissionByPid(Integer id);

    List<Integer> queryHasResources(Integer roleId);

	/**
	 * 根据角色ID查询角色拥有的权限
	 */
	List<Permission> queryPermissionByUserId(Integer id);

	/**
	 * 根据角色ID查询角色拥有的权限编码
	 */
	List<String> getCodeByUserId(Integer id);
}
