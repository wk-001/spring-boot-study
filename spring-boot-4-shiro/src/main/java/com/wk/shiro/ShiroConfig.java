package com.wk.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.wk.redis.RedisCacheManager;
import com.wk.redis.RedisManager;
import com.wk.redis.RedisSessionDAO;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

/**
 * shiro配置类
 */
@Configuration
public class ShiroConfig {

	/**
	 * shiro请求过滤器
	 * Web应用中,Shiro可控制的Web请求必须经过Shiro主过滤器的拦截
	 */

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//必须设置 SecurityManager,Shiro的核心安全接口
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		//这里的/toLogin是controller的映射路径,非页面，如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/toLogin");
		//这里的/index是controller的映射路径,非页面,登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/");
		//未授权的URL，只针对本方法中配置的路径
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		//自定义拦截器限制并发人数
		LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
		//限制同一帐号同时在线的个数
		filtersMap.put("kickout", kickoutSessionControlFilter());
		/*配置自定义登出 覆盖 logout 之前默认的LogoutFilter
		如果退出页面固定可以直接在controller层写一个logout方法
		filtersMap.put("logout", shiroLogoutFilter());*/
		//统计登录人数
		shiroFilterFactoryBean.setFilters(filtersMap);

		// 配置访问权限 必须是LinkedHashMap，因为它保证有序
		// 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 一定要注意顺序,否则无法达到过滤效果
		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 定义filterChain，配置不登录可以访问的资源，anon 表示资源可以匿名访问,静态资源不拦截
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/plugin/**", "anon");
		// druid数据源监控页面不拦截
		filterChainDefinitionMap.put("/druid/**", "anon");
		// 配置退出过滤器，其中具体的退出代码Shiro已经替我们实现了，替换为自定义退出
//		filterChainDefinitionMap.put("/logout", "logout");
		//访问项目首页不限制
		filterChainDefinitionMap.put("/", "anon");
		//访问登录方法不受限制
		filterChainDefinitionMap.put("/login", "anon");
		// 除上以外所有url都必须认证通过才可以访问，未通过认证自动访问LoginUrl
//		filterChainDefinitionMap.put("/**", "authc");
		//访问/**下的资源 首先要通过 kickout 后面的filter，然后再通过user后面对应的filter才可以访问。
		// user：登录或记住我登录都可以
		//kickout：开启限制同一账号登录
		filterChainDefinitionMap.put("/**", "kickout,user");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 配置LogoutFilter，如果退出页面固定可以直接在controller层写一个logout方法
	 * @return

	public ShiroLogoutFilter shiroLogoutFilter(){
		ShiroLogoutFilter shiroLogoutFilter = new ShiroLogoutFilter();
		//配置登出后重定向的地址
		shiroLogoutFilter.setRedirectUrl("/login");
		return shiroLogoutFilter;
	}*/

	//手动创建ShiroRealm类，需要继承shiro提供的realm,提供用户的身份和权限信息
	@Bean
	public ShiroRealm shiroRealm(){
		// 配置Realm，需自己实现
		ShiroRealm shiroRealm = new ShiroRealm();
		//开启缓存
		shiroRealm.setCachingEnabled(true);
		//启用身份验证缓存，即缓存AuthenticationInfo信息，默认false
		shiroRealm.setAuthenticationCachingEnabled(true);
		//缓存AuthenticationInfo信息的缓存名称 在ehcache-shiro.xml中有对应缓存的配置
		shiroRealm.setAuthenticationCacheName("authenticationCache");
		//启用授权缓存，即缓存AuthorizationInfo信息，默认false
		shiroRealm.setAuthorizationCachingEnabled(true);
		//缓存AuthorizationInfo信息的缓存名称  在ehcache-shiro.xml中有对应缓存的配置
		shiroRealm.setAuthorizationCacheName("authorizationCache");
		//配置密码比较器
        shiroRealm.setCredentialsMatcher(credentialsMatcher());
		return shiroRealm;
	}

	/**
	 * 配置核心安全事务管理器
	 * @return
	 */
	@Bean
	public SecurityManager securityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 配置SecurityManager，并注入自定义的shiroRealm
		securityManager.setRealm(shiroRealm());
		//设置记住我
		securityManager.setRememberMeManager(rememberMeManager());
		//设置Redis缓存
		securityManager.setCacheManager(redisCacheManager());
		//设置session管理器
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	//shiro生命周期处理器
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}

	/*配置密码比较器
	@Bean
	public HashedCredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }*/

	//配置密码比较器和密码重试次数
    @Bean
	public RetryLimitHashedCredentialsMatcher credentialsMatcher(){
		RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher();
		//设置RedisManager
		credentialsMatcher.setRedisManager(redisManager());
		//设置加密算法
		credentialsMatcher.setHashAlgorithmName("MD5");
		//加密次数
		credentialsMatcher.setHashIterations(2);
		//存储为16进制
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}

    //thymeleaf页面使用shiro标签
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	//cookie对象。会话Cookie模板默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe，自定义
	public SimpleCookie rememberMeCookie(){
		// 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberMe"/>
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		//设置cookie过期时间，单位：秒,有效期30天
		cookie.setMaxAge(2592000);
		/*setcookie的httponly属性设置为true，会增加对xss防护的安全系数
		只能通过http访问，javascript无法访问
		防止xss读取cookie*/
		cookie.setHttpOnly(true);
		return cookie;
	}

	//管理cookie对象
	public CookieRememberMeManager rememberMeManager(){
		CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
		rememberMeManager.setCookie(rememberMeCookie());
		//rememberMe cookie加密的密钥
		rememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
		return rememberMeManager;
	}

	//开启shiro注解
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	//配置Redis缓存
	public RedisCacheManager redisCacheManager(){
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		//redis中针对不同用户缓存,user对象的userName属性
		redisCacheManager.setPrincipalIdFieldName("userName");
		//用户权限信息缓存时间
		redisCacheManager.setExpire(200000);
		return redisCacheManager;
	}

	@Bean
	public RedisManager redisManager(){
		RedisManager redisManager = new RedisManager();
		return redisManager;
	}

	/**
	 * 解决： 无权限页面不跳转 shiroFilterFactoryBean.setUnauthorizedUrl("/403") 无效
	 * shiro的源代码ShiroFilterFactoryBean.Java定义的filter必须满足filter instanceof AuthorizationFilter，
	 * 只有perms，roles，ssl，rest，port才是属于AuthorizationFilter，而anon，authcBasic，auchc，user是AuthenticationFilter，
	 * 所以unauthorizedUrl设置后页面不跳转 Shiro注解模式下，登录失败与没有权限都是通过抛出异常。
	 * 并且默认并没有去处理或者捕获这些异常。在SpringMVC下需要配置捕获相应异常来通知用户信息
	 *
	 * 和GlobalExceptionHandler.handleAuthorizationException()二选一使用
	 */
	@Bean
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver simpleMappingExceptionResolver=new SimpleMappingExceptionResolver();
		Properties properties=new Properties();
		//这里的 /unauthorized 是页面，不是访问的路径
		properties.setProperty("org.apache.shiro.authz.UnauthorizedException","/403");
		properties.setProperty("org.apache.shiro.authz.UnauthenticatedException","/403");
		simpleMappingExceptionResolver.setExceptionMappings(properties);
		return simpleMappingExceptionResolver;
	}

	/**
	 * 配置session监听
	 */
	@Bean
	public SessionListener sessionListener(){
		SessionListener sessionListener = new ShiroSessionListener();
		return sessionListener;
	}

	/**
	 * 配置会话ID生成器
	 */
	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}

	/**
	 * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
	 * MemorySessionDAO 直接在内存中进行会话维护
	 * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 */
	@Bean
	public SessionDAO sessionDAO(){
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		//session在redis中的保存时间,最好大于session会话超时时间
		redisSessionDAO.setExpire(12000);
		//sessionId生成器
		redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
		return redisSessionDAO;
	}

	/**
	 * 配置保存sessionId的cookie
	 * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
	 */
	@Bean
	public SimpleCookie sessionIdCookie(){
		//设置cookie的名称
		SimpleCookie simpleCookie = new SimpleCookie("sid");
		/*setcookie的httponly属性设置为true，会增加对xss防护的安全系数
		只能通过http访问，javascript无法访问
		防止xss读取cookie*/
		simpleCookie.setHttpOnly(true);
		//-1表示浏览器关闭时此cookie失效
		simpleCookie.setMaxAge(-1);
		return simpleCookie;
	}

	//配置会话管理器，设定会话超时及保存
	@Bean
	public SessionManager sessionManager(){
//		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		ShiroSessionManager sessionManager = new ShiroSessionManager();
		List<SessionListener> listeners = new ArrayList<>();

		//配置监听
		listeners.add(sessionListener());
		sessionManager.setSessionListeners(listeners);
		sessionManager.setSessionIdCookie(sessionIdCookie());
		sessionManager.setSessionDAO(sessionDAO());
		sessionManager.setCacheManager(redisCacheManager());
		sessionManager.setSessionFactory(sessionFactory());

		//全局会话超时时间(单位：毫秒)，默认30分钟，暂时设置10秒钟用来测试
//		sessionManager.setGlobalSessionTimeout(10000);
		//删除无效session对象 默认为true
//		sessionManager.setDeleteInvalidSessions(true);
		//开启定时调度器进行检测过期session 默认为true
//		sessionManager.setSessionValidationSchedulerEnabled(true);
		//设置session失效的扫描时间(单位：毫秒), 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时 暂时设置10秒钟用来测试
//		sessionManager.setSessionValidationInterval(10000);
		//取消url 后面的 JSESSIONID
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		return sessionManager;
	}

	/**
	 * 并发控制登录
	 */
	@Bean
	public KickoutSessionControlFilter kickoutSessionControlFilter(){
		KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
		//根据会话ID获取会话进行踢出操作
		kickoutSessionControlFilter.setSessionManager(sessionManager());
		//CacheManager获取相应的cache来缓存用户登录的会话，用于保存用户和会话之间的关系
		kickoutSessionControlFilter.setRedisManager(redisManager());
		//是否踢出后登录的，默认false，即后登录的用户踢出之前登录的用户
		kickoutSessionControlFilter.setKickoutAfter(false);
		//同一个用户最大会话数，默认1，即同一个用户同一时间只允许一个人登录
		kickoutSessionControlFilter.setMaxSession(1);
		//被踢出后重定向的地址
		kickoutSessionControlFilter.setKickoutUrl("/login?kickout=1");
		return kickoutSessionControlFilter;
	}

	/**
	 * 解决spring-boot Whitelabel Error Page
	 */
	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
		return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
			@Override
			public void customize(ConfigurableWebServerFactory factory) {
				ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/403.html");
				ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
				ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

				factory.addErrorPages(error401Page, error404Page, error500Page);
			}
		};
	}

	@Bean("sessionFactory")
	public ShiroSessionFactory sessionFactory(){
		ShiroSessionFactory sessionFactory = new ShiroSessionFactory();
		return sessionFactory;
	}
}
