package com.wk.sys.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.alibaba.druid.util.StringUtils;
import lombok.Data;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration		//声明本类是配置类
@ConditionalOnClass(value = {SecurityManager.class})	//必须有SecurityManager存在
@ConditionalOnWebApplication(type = Type.SERVLET)		//web环境下本配置类生效
@ConfigurationProperties(prefix = "shiro")				//通过配置文件中注入值,需要configuration-processor依赖
@Data		//通过get set注入值
public class ShiroAutoConfiguration {

    private static final String SHIRO_FILTER = "shiroFilter";

    private String hashAlgorithmName = "MD5";	//加密方式

    private int hashIterations = 2;	//散列次数

    private String loginUrl = "/";		//未登录状态下需要跳转的页面

    private String logoutUrl="/logout";		//登出路径

    private String[] anonUrls;		//放行路径

    private String[] authcUrls;	// 拦截路径

    //声明密码匹配器
    @Bean
    public HashedCredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(hashAlgorithmName);
        credentialsMatcher.setHashIterations(hashIterations);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    /**
     * 声明自己定义的userRealm
     * 方法添加@Bean注解后，形式参数会默认加上@Autowired注解，并从IOC容器中获取之前定义的credentialsMatcher对象
     */
    @Bean
    public UserRealm userRealm(CredentialsMatcher credentialsMatcher){
        UserRealm userRealm = new UserRealm();
        //注入密码匹配器
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }

    /**
     * 配置SecurityManager安全管理器
     * 方法添加@Bean注解后，形式参数会默认加上@Autowired注解，并从IOC容器中获取之前定义的userRealm对象
     */
    @Bean
    public SecurityManager securityManager(UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //注入userRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    /**
     * 配置shiro过滤器
     * 方法添加@Bean注解后，形式参数会默认加上@Autowired注解，并从IOC容器中获取之前定义的securityManager对象
     */
    @Bean(SHIRO_FILTER)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        //注入安全管理器
        factoryBean.setSecurityManager(securityManager);
//		设置未登录状态下要跳转的页面
        factoryBean.setLoginUrl(loginUrl);
        Map<String, String> map = new HashMap<>();
//		设置放行路径
        if(anonUrls!=null && anonUrls.length>0){
            for (String anonUrl : anonUrls) {
                map.put(anonUrl, "anon");
            }
        }
//		设置登出路径
        if(!StringUtils.isEmpty(logoutUrl)){
            map.put(logoutUrl, "logout");
        }
//		设置拦截路径
        if(authcUrls!=null && authcUrls.length>0){
            for (String authcUrl : authcUrls) {
                map.put(authcUrl, "authc");
            }
        }

//		设置过滤链
        factoryBean.setFilterChainDefinitionMap(map);
        return factoryBean;
    }

    /**
     * 注册shiro的委托过滤器，相当于之前在web.xml里面配置的
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<DelegatingFilterProxy>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName(SHIRO_FILTER);
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    //使用shiro在controller层的注解 开始
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    //使用shiro在controller层的注解 结束

    //HTML页面使用shiro标签
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
