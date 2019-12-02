package com.wk.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.sys.entity.Permission;
import com.wk.sys.mapper.PermissionMapper;
import com.wk.sys.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {


	@Override
	public Integer getMaxOrderNum() {
		return this.getBaseMapper().getMaxOrderNum();
	}

	@Override
	public void deleteMenuById(Integer id) {
		super.removeById(id);
		this.getBaseMapper().deleteRolePermissionByPid(id);
	}

    @Override
    public List<Integer> queryHasResources(Integer roleId) {
        return this.getBaseMapper().queryHasResources(roleId);
    }
}
