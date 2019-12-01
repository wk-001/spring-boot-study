package com.wk.sys.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wk.sys.common.ActiverUser;
import com.wk.sys.entity.User;
import com.wk.sys.service.PermissionService;
import com.wk.sys.service.RoleService;
import com.wk.sys.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;



	//登录认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("loginname",token.getPrincipal().toString());
		User user = userService.getOne(wrapper);
		if (user != null) {
			ActiverUser activerUser = new ActiverUser();
			activerUser.setUser(user);
			ByteSource salt = ByteSource.Util.bytes(user.getSalt());
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser,user.getPwd(),salt,getName());
			return info;
		}
		return null;
	}

	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
		return null;
	}

}
