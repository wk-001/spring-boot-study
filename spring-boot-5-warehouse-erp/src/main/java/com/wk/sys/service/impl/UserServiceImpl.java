package com.wk.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wk.sys.entity.User;
import com.wk.sys.mapper.UserMapper;
import com.wk.sys.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.sys.vo.UserVo;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Override
	public IPage<User> queryList(Page<User> page, UserVo userVo) {
		return this.getBaseMapper().queryList(page, userVo);
	}
}
