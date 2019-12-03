package com.wk.sys.mapper;

import com.wk.sys.entity.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wk
 * @since 2019-12-02
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    void insertRolePermission(@Param("roleId") Integer roleId,@Param("ids") Integer[] ids);
}
