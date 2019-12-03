package com.wk.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wk.sys.entity.RolePermission;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wk
 * @since 2019-12-02
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 重置角色和权限的关系
     */
    void assignPermission(Integer roleId, Integer[] ids);
}
