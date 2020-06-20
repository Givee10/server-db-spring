package com.givee.demo.server.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class BeanUtil implements ApplicationContextAware {
	private static ApplicationContext context;

	public static <T> T getBean(Class<T> beanClass) {
		return context.getBean(beanClass);
	}

	public static <T> T getBean(Class<T> beanClass, Object... params) {
		return context.getBean(beanClass, params);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
}
