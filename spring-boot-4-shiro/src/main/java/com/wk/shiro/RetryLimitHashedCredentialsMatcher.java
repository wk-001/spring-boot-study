package com.wk.shiro;

import com.wk.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录次数限制
 */
@Slf4j
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

	public static final String SHIRO_LOGIN_COUNT = "shiro_login_count";

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
		AtomicInteger retryCount = new AtomicInteger(0);
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
		}
		return false;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		String str = "login_count";
		byte[] bytes = str.getBytes("utf-8");
		System.out.println("bytes = " + Arrays.toString(bytes));
	}
}
