package com.wk.sys.mapper;

import com.wk.sys.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}
