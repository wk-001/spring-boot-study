package com.wk.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.sys.entity.Role;
import com.wk.sys.mapper.RoleMapper;
import com.wk.sys.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

	@Override
	public void removeRoleById(Integer id) {
		super.removeById(id);
		this.getBaseMapper().deleteUserRoleByRid(id);
		this.getBaseMapper().deleteRolePermissionByRid(id);
	}
}
