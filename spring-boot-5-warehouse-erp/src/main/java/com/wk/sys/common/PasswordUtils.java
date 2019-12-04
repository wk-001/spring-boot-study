package com.wk.sys.common;

import com.wk.sys.entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

public class PasswordUtils {

	/**
	 * 对用户输入的密码进行加密
	 * @param user
	 * @return
	 */
	public static User encryptPassword(User user) {
		String salt = new SecureRandomNumberGenerator().nextBytes().toString();
		int times = 2;
		String algorithmName = "MD5";
		String encodedPassword = new SimpleHash(algorithmName, user.getPwd(), salt, times).toString();
		user.setPwd(encodedPassword);
		user.setSalt(salt);
		return user;
	}

}
