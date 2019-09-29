package com.wk.shiro;

import com.wk.pojo.Resource;
import com.wk.pojo.Role;
import com.wk.pojo.User;
import com.wk.service.ResourceService;
import com.wk.service.RoleService;
import com.wk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

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
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		User user = (User) principal.getPrimaryPrincipal();
		Integer userId = user.getId();
		log.info("用户："+user.getUserName()+"开始授权");

		//根据ID获取用户所拥有的角色
		List<Role> roleList = roleService.getAllRolesByUserId(userId);
		//获取用户角色编码
		Set<String> roleSet = new HashSet<>();
		for (Role role : roleList) {
			roleSet.add(role.getCode());
		}

		//根据ID获取用户所拥有的权限
		List<Resource> resourceList = resourceService.getAllResourcesByUserId(userId);
		//获取用户具有的资源标识
		Set<String> resourceSet = new HashSet<>();
		for (Resource resource : resourceList) {
			resourceSet.add(resource.getPermission());
		}

		//将用户的角色编码和资源标识放入认证信息，方便判断
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(roleSet);
		info.setStringPermissions(resourceSet);
		return info;
	}

}
