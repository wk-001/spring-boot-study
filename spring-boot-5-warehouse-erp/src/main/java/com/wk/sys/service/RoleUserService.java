package com.wk.sys.service;

import com.wk.sys.entity.RoleUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wk
 * @since 2019-12-02
 */
public interface RoleUserService extends IService<RoleUser> {

    void editUserRole(Integer userId, Integer[] ids);
}
