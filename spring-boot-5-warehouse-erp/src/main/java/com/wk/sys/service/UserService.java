package com.wk.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wk.sys.vo.UserVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wk
 * @since 2019-11-29
 */
public interface UserService extends IService<User> {

	IPage<User> queryList(Page<User> page, UserVo userVo);

	void deleteUserById(Integer id);

	Integer getMaxOrderNum();
}
