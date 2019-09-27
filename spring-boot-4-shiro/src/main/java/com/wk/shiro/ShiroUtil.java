package com.wk.shiro;

import com.wk.pojo.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;


public class ShiroUtil {
	
	/**
	 * 对用户输入的密码进行加密
	 * @param user
	 * @return
	 */
	public static User encryptPassword(User user) {
		String salt = new SecureRandomNumberGenerator().nextBytes().toString();
		int times = 2;
		String encodedPassword = new SimpleHash("MD5", user.getPassword(), salt, times).toString();
		user.setPassword(encodedPassword);
		user.setSalt(salt);
		return user;
	}
}
