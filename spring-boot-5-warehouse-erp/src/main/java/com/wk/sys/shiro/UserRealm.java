package com.wk.sys.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wk.sys.common.ActiverUser;
import com.wk.sys.common.Constast;
import com.wk.sys.entity.User;
import com.wk.sys.service.PermissionService;
import com.wk.sys.service.RoleService;
import com.wk.sys.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

public class UserRealm extends AuthorizingRealm {

	@Autowired
	@Lazy		//使用的时候才会加载
	private UserService userService;

	@Autowired
	@Lazy		//使用的时候才会加载
	private RoleService roleService;

	@Autowired
	@Lazy		//使用的时候才会加载
	private PermissionService permissionService;



	//登录认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("loginname",token.getPrincipal().toString());
		User user = userService.getOne(wrapper);
		if (user != null) {
			//设置用户信息
			ActiverUser activerUser = new ActiverUser();
			activerUser.setUser(user);
			ByteSource salt = ByteSource.Util.bytes(user.getSalt());

			//根据用户ID获取用户权限编码
			List<String> persmissions = permissionService.getCodeByUserId(user.getId());
			activerUser.setPermissions(persmissions);

			//根据用户ID获取用户角色编码
			List<String> roles = roleService.getCodeByUserId(user.getId());
			activerUser.setRoles(roles);

			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser,user.getPwd(),salt,getName());
			return info;
		}
		return null;
	}

	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		ActiverUser activerUser = (ActiverUser) principals.getPrimaryPrincipal();
		User user = activerUser.getUser();
		List<String> permissions = activerUser.getPermissions();
		List<String> roles = activerUser.getRoles();
		if(user.getType()== Constast.USER_TYPE_SUPER){
			info.addStringPermission("*:*");		//超级管理员有所有的权限
		}else{
			if (permissions != null && permissions.size()>0) {
				info.addStringPermissions(permissions);
			}
		}
		info.addRoles(roles);
		return info;
	}

}
