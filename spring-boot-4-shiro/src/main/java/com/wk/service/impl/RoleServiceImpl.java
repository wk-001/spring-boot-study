package com.wk.service.impl;

import com.wk.pojo.Role;
import com.wk.dao.RoleMapper;
import com.wk.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-09-29
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<Role> getAllRolesByUserId(int userId) {
		return roleMapper.getAllRolesByUserId(userId);
	}
}
