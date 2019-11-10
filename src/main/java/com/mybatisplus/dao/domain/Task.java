package com.mybatisplus.dao.domain;

import javax.persistence.Transient;


public class Task {
	
	/**
	 * 账号认证
	 */
	@Transient
	private LoginAuth loginAuth;
	
	/**
	 * 采集的延迟时间
	 * 1000 = 1秒
	 * 
	 */
	@Transient
	private Integer delay;
	
	/**
	 * 记录时间,最后的运行完毕后的时间
	 */



	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}


	/**
	 * 是否通过了延迟时间，true可以跑，false还不能跑
	 * @return
	 */
	public boolean isCanRun(){
		
		if(this.loginAuth.getLastTime()==null){
			return true;
		}
		
		long currentDelay = System.currentTimeMillis()-this.loginAuth.getLastTime() ;
		if( currentDelay >= delay ){
			return true ;
		}
		
		return false ;
	}
	
	public Long getWaitTime(){
		long currentDelay = System.currentTimeMillis()-this.loginAuth.getLastTime() ;
			
		return this.delay-currentDelay;
	}
	
	public LoginAuth getLoginAuth() {
		return loginAuth;
	}

	public void setLoginAuth(LoginAuth loginAuth) {
		this.loginAuth = loginAuth;
	}
	
	
}
