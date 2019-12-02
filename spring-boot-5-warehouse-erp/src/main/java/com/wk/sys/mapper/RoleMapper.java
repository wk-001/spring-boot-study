package com.wk.sys.mapper;

import com.wk.sys.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
public interface RoleMapper extends BaseMapper<Role> {

	void deleteUserRoleByRid(Integer id);

	void deleteRolePermissionByRid(Integer id);
}
