package com.feigua.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.crawler.HotVideoListHandler;
import com.feigua.login.LoginService;
import com.mybatisplus.dao.domain.LoginAuth;

@Configuration
public class Environment {
	
	private static Logger logger= LoggerFactory.getLogger(Environment.class);
	
	public static Object LOGIN_LOCK =new Object() ;   //登录认证锁
	
	public static Object WAIT_LOCK =new Object() ;   //等待锁
	 
	@Autowired
	private LoginService loginService;
	
	//空闲的账号的队列 ,运行后又放回的账号
	public static final ConcurrentLinkedQueue <LoginAuth> freeLoginAuthQueue  = new ConcurrentLinkedQueue<>();
	
	//正在运行的账号
//	public static final List<LoginAuth> runLoginAuthQueue  = new ArrayList<>();
	
	//认证使用完毕后，返回空闲队列
	public static void backLoginAuth(LoginAuth loginAuth) {
		if(!freeLoginAuthQueue.contains(loginAuth)){
			freeLoginAuthQueue.add(loginAuth);
		}
	}
	
	//移除登录认证
	public static void removeLoginAuth(LoginAuth loginAuth) {
		freeLoginAuthQueue.remove(loginAuth);
	}
	
	/**
	 * 获取一个活跃状态的账号认证，用于采集任务
	 * @return
	 */
	public  LoginAuth getActiveLoginAuth() {
		
		logger.info("账号数量:"+freeLoginAuthQueue.size());
		
		//认证队列为空，需要重新刷新数据库的登录账号; 去除掉正在运行中的账号
		if(freeLoginAuthQueue.isEmpty()){ 
			Map<String, Integer> params = new HashMap<String, Integer>();
			params.put("status", 1);
			loginService.loginAccount(params);
		}
		
		while (!freeLoginAuthQueue.isEmpty()) {
			LoginAuth loginAuth =  freeLoginAuthQueue.peek(); //先不要弹出
			boolean flag = loginService.processLogin(loginAuth); //内部会做登录，会重新放进去，所以不能先弹出
			//验证登录账号
			//如果认证是失败，那就没必要存放在空闲队列
			if(flag==false){
				logger.info("返回认证为false");
				continue;
			}else{
				loginAuth = freeLoginAuthQueue.poll(); //弹出对堆栈 
				//时间如果大于延迟20分钟，清空访问次数的限制
				long min = getBetween(loginAuth);
				logger.error(new Date()+"|"+loginAuth.getLastDatetime() +"相聚时间"+min);
				if(min>loginAuth.getDelay()){ //超过了等待的时间
					loginAuth.setVisits(0);
				}
				
				return  loginAuth ;
			}
		}
		return null;
	}

	private   long getBetween(LoginAuth loginAuth) {
		long time = new Date().getTime()-loginAuth.getLastDatetime().getTime() ;
		System.out.println(time);
		return time / (1000 * 60);
		
	}
	
	 
	
	
}
	
	
		
