package com.wk.sys.service;

import com.wk.sys.common.ResultObj;
import com.wk.sys.entity.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

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
