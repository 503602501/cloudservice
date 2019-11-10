package com.feigua.entity;


public class Constant {
	
	public  static String BLOGGER_DETAIL = "blogger_detail";
	
	public  static String DEFAULT = "default";
	
	
	/**
	 * login
	 * 账号的状态
	 */
	public  static Integer LOGIN_AUTH_INIT = 0; //未初始化
	public  static Integer LOGIN_AUTH_ACTIVE = 1; //在线活跃
	public  static Integer LOGIN_AUTH_EXPIRE= 2; //过期
	public  static Integer LOGIN_AUTH_EXCEPTION= 3; //异常,不作为运行的账号
	public  static Integer LOGIN_AUTH_STOP= 4; //延迟停止
	
	
	/**
	 * HOT_VIDEO
	 *热门列表
	 *0未处理，1处理了博主详情，2正在处理，3异常情况，4无需处理
	 */
	public  static Integer BUSINESS_INIT =0; //未初始化业务
	public  static Integer BUSINESS_DONE =1; //已经处理
	public  static Integer BUSINESS_DONEING =2; //正在处理
	public  static Integer BUSINESS_EXCEPTION =3; //异常
	public  static Integer BUSINESS_NOT_NEED =4; //无需处理
	
}
