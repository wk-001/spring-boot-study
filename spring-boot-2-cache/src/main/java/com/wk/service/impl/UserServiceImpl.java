package com.wk.service.impl;

import com.wk.dao.UserDAO;
import com.wk.pojo.User;
import com.wk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userMapper;

	@Override
	public List<User> selectAll() {
		return userMapper.selectByExample(null);
	}

	@Override
	public User getById(int id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public User updUser(User user) {
		userMapper.updateByPrimaryKeySelective(user);
		return user;
	}

	@Override
	public void delUser(int id) {
		userMapper.deleteByPrimaryKey(id);
	}
}
