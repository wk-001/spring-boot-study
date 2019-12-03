package com.wk.sys.service.impl;

import com.wk.sys.common.ResultObj;
import com.wk.sys.entity.RolePermission;
import com.wk.sys.mapper.RolePermissionMapper;
import com.wk.sys.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-12-02
 */
@Service
@Transactional
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Override
    public void assignPermission(Integer roleId, Integer[] ids) {
        //先删除原有的角色权限关系
        Map<String,Object> map = new HashMap<>();
        map.put("rid",roleId);
        this.getBaseMapper().deleteByMap(map);
        //再重新添加角色和权限的关系
        this.getBaseMapper().insertRolePermission(roleId,ids);
    }

}
