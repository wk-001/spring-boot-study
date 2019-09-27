package com.wk.shiro;

import com.wk.pojo.User;
import com.wk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName = token.getPrincipal().toString();
		log.info("用户："+userName+"开始认证");
		//通过用户名查找用户，密码已加密，无法直接查找
		User user = userService.findByUserName(userName);
		if (user != null) {
			String DBPassword = user.getPassword();
			String salt = user.getSalt();
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,DBPassword, ByteSource.Util.bytes(salt),getName());
			return info;
		}else {
			throw new UnknownAccountException("用户名或密码错误！");
		}
	}

	/**
	 * 获取用户角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		return null;
	}

}
