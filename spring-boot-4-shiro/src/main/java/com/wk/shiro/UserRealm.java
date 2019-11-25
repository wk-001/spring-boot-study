package com.wk.shiro;

import com.wk.pojo.Resource;
import com.wk.pojo.Role;
import com.wk.pojo.User;
import com.wk.service.ResourceService;
import com.wk.service.RoleService;
import com.wk.service.UserService;
import com.wk.util.ActiverUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 解决授权方法需要频繁查询数据库的问题，除了将角色权限信息放入缓存，还有另一种简单的解决思路
 * 用户认证方法只在登录时查询一次，而认证通过的info对象可以一直存在。
 * 封装一个对象，在用户认证通过后查询用户、角色、权限信息，放入认证通过的info对象，
 * 就可以解决频繁查询用户角色权限的问题
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

	/**
	 * 登录认证，验证用户身份
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName = token.getPrincipal().toString();
		log.info("用户："+userName+"开始认证");
		//通过用户名查找用户，密码已加密，无法直接查找
		User user = userService.findByUserName(userName);
		if (user != null) {
			Integer userId = user.getId();
			String DBPassword = user.getPassword();
			String salt = user.getSalt();
			//根据ID获取用户所拥有的角色
			List<Role> roleList = roleService.getAllRolesByUserId(userId);
			List<String> roles = new ArrayList<>();
			for (Role role : roleList) {
				roles.add(role.getCode());
			}
			//根据ID获取用户所拥有的权限
			List<Resource> resourceList = resourceService.getAllResourcesByUserId(userId);
			List<String> resources = new ArrayList<>();
			for (Resource resource : resourceList) {
				resources.add(resource.getPermission());
			}
			ActiverUser activerUser = new ActiverUser(user,roles,resources);
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser,DBPassword, new MyByteSource(salt),getName());
			log.info("用户："+userName+"认证成功");
			return info;
		}else {
			throw new UnknownAccountException("用户名或密码错误！");
		}
	}

	/**
	 * 获取用户角色和权限
	 * 授权用户权限
	 * 授权的方法是在碰到<shiro:hasPermission name=''></shiro:hasPermission>标签
	 * 或@RequiresPermissions，@RequiresRoles注解的时候调用的
	 * 它会去检测shiro框架中的权限(这里的permissions)是否包含有该标签的name值,如果有,里面的内容显示
	 * 如果没有,里面的内容不予显示(这就完成了对于权限的认证.)
	 *
	 * shiro的权限授权是通过继承AuthorizingRealm抽象类，重载doGetAuthorizationInfo();
	 * 当访问到页面的时候，链接配置了相应的权限或者shiro标签才会执行此方法否则不会执行
	 * 所以如果只是简单的身份认证没有权限的控制的话，那么这个方法可以不进行实现，直接返回null即可。
	 *
	 * 在这个方法中主要是使用类：SimpleAuthorizationInfo 进行角色的添加和权限的添加。
	 *
	 * 也可以添加set集合：roles是从数据库查询的当前用户的角色，stringPermissions是从数据库查询的当前用户对应的权限
	 * authorizationInfo.setRoles(roleSet); authorizationInfo.setStringPermissions(resourceSet);
	 *
	 * 就是说如果在shiro配置文件中添加了filterChainDefinitionMap.put("/add", "perms[权限添加]");
	 * 就说明访问/add这个链接必须要有“权限添加”这个权限才可以访问
	 *
	 * 如果在shiro配置文件中添加了filterChainDefinitionMap.put("/add", "roles[100002]，perms[权限添加]");
	 * 就说明访问/add这个链接必须要有 "权限添加" 这个权限和具有 "100002" 这个角色才可以访问
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		ActiverUser user = (ActiverUser) principal.getPrimaryPrincipal();
		log.info("用户："+user.getUser().getUserName()+"开始授权");

		//将用户的角色编码和资源标识放入认证信息，方便判断
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRoles(user.getRoles());
		info.addStringPermissions(user.getPermissions());
		log.info("用户："+user.getUser().getUserName()+"授权成功");
		return info;
	}

	/**
	 * 重写方法,清除当前用户的的 授权缓存
	 */
	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 重写方法，清除当前用户的认证缓存，因为shiroRealm中存储的是user对象，Redis缓存是以用户名存储的
	 * 需要特殊处理
	 */
	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(new SimplePrincipalCollection(principals, getName()));
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	/**
	 * 自定义方法：清除所有 授权缓存
	 */
	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	/**
	 * 自定义方法：清除所有 认证缓存
	 */
	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	/**
	 * 自定义方法：清除所有的  认证缓存  和 授权缓存
	 */
	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

}
