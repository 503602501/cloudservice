package com.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.async.AsyncTaskService;
import com.feigua.entity.Environment;
import com.feigua.login.LoginService;
import com.mybatisplus.dao.domain.LoginAuth;
import com.mybatisplus.dao.domain.Task;


/**
 * 容器初始化后自动登录认证账号
 * 
 * @author rocky
 * IHandler,
 */
@Component
public class LoginApplicationListener implements  ApplicationListener<ContextRefreshedEvent>{

	
	private static Logger logger= LoggerFactory.getLogger(HotVideoListHandler.class);
	
	@Autowired
	private LoginService loginService ;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent paramE) {
		try {
			loginService.loginAccount();
		} catch (Exception e) {
			logger.info("开启应用的登录认证.......",e);
			e.printStackTrace();
		}
	}
	 
}
