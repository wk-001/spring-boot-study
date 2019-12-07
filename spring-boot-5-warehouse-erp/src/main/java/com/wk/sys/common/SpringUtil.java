package com.wk.sys.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext act) throws BeansException {
		applicationContext=act;
	}
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取IOC容器中的bean对象
	 * 用法：User user = SpringUtil.getBean(User.class);
	 */
	public static <T> T getBean(Class<T> cls) {
		return applicationContext.getBean(cls);
	}

}
