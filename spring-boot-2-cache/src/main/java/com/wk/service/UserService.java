package com.wk.service;

import com.wk.pojo.User;

import java.util.List;

public interface UserService {

	List<User> selectAll();

	User getById(int id);

}
