package com.wk.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * shiro配置类
 */
@Configuration
public class ShiroConfig {

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//设置securityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		//登录的URL
		shiroFilterFactoryBean.setLoginUrl("/login");
		//登录成功后跳转的URL
		shiroFilterFactoryBean.setSuccessUrl("/index");
		//未授权的URL
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 定义filterChain，静态资源不拦截
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		// druid数据源监控页面不拦截
		filterChainDefinitionMap.put("/druid/**", "anon");
		// 配置退出过滤器，其中具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		//访问项目首页不限制
		filterChainDefinitionMap.put("/", "anon");
		// 除上以外所有url都必须认证通过才可以访问，未通过认证自动访问LoginUrl
		filterChainDefinitionMap.put("/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	//手动创建ShiroRealm类，需要继承shiro提供的realm
	@Bean
	public ShiroRealm shiroRealm(){
		// 配置Realm，需自己实现
		ShiroRealm shiroRealm = new ShiroRealm();
		return shiroRealm;
	}

	@Bean
	public SecurityManager securityManager(){
		// 配置SecurityManager，并注入shiroRealm
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm());
		return securityManager;
	}


}
