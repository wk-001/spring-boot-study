package com.wk.dao;

import com.wk.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-09-29
 */
public interface RoleMapper extends BaseMapper<Role> {

	List<Role> getAllRolesByUserId(int userId);
}
