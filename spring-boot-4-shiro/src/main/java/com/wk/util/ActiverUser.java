package com.wk.util;

import com.wk.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户认证方法只在登录时执行一次，而授权方法需要查询多次
 * 解决方法：封装对象，放入用户的信息以及用户的角色、权限信息
 * 用户认证通过后查询用户的角色权限信息封装到本对象中，放入到认证的info对象，认证的时候只需要获取
 * info对象中的角色权限信息即可，不需要频繁查询数据库
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser {

	private User user;
	
	private List<String> roles;
	
	private List<String> permissions;
}
