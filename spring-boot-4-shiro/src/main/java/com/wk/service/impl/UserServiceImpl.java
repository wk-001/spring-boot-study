package com.wk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wk.dao.UserMapper;
import com.wk.pojo.User;
import com.wk.service.UserService;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wk
 * @since 2019-09-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User findByUserName(String userName) {
		QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_name",userName);
		User user = userMapper.selectOne(wrapper);
		if (user != null) {
			if(1==user.getStatus()){
				return user;
			}else {
				throw new LockedAccountException("账号已被锁定,请联系管理员！");
			}
		}else {
			throw new UnknownAccountException("用户名不存在！");
		}
	}
}
