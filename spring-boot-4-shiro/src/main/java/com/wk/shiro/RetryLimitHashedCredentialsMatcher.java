package com.wk.shiro;

import com.wk.dao.UserMapper;
import com.wk.redis.RedisManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录次数限制
 */
@Slf4j
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

	//存放登录次数
	public static final String SHIRO_LOGIN_COUNT = "shiro_login_count:";

	/*如果登录失败超过指定次数，就在缓存中存放一条数据，过期时间一个小时，
	登录前判断，如果该前缀+用户名的数据存在，就抛出异常*/
	public static final String SHIRO_LOGIN_NOT_MATCH = "shiro_login_not_match:";

	@Autowired
	private UserMapper userMapper;

	//密码匹配器中传入的RedisManager
	private RedisManager redisManager;

	public void setRedisManager(RedisManager redisManager) {
		this.redisManager = redisManager;
	}

	//获取缓存中的用户登录次数
	private String loginCountKey(String username) {
		return this.SHIRO_LOGIN_COUNT + username;
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		//获取用户名
		String userName = token.getPrincipal().toString();
		//获取用户登录次数
		AtomicInteger retryCount = (AtomicInteger)redisManager.get(loginCountKey(userName));
		if (retryCount == null) {
			//如果用户没有登录过，登录次数+1
			retryCount = new AtomicInteger(0);
			log.info("密码重试次数"+retryCount);
		}
		if(retryCount.incrementAndGet()>3){
			log.info("密码重试次数超过3次");
			//抛出尝试次数过多异常
			throw new ExcessiveAttemptsException();
		}
		//判断用户账号和密码是否正确
		boolean match = super.doCredentialsMatch(token, info);
		if (match) {
			//如果正确，清除缓存中用户登录次数
			redisManager.del(loginCountKey(userName));
		}else {
			//密码不匹配就把登录次数放入缓存
			redisManager.set(loginCountKey(userName),retryCount);
		}
		return match;
	}

}
