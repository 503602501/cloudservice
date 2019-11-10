package com.feigua.entity;

import java.util.Date;

import javax.xml.stream.events.EndDocument;

import com.mybatisplus.dao.domain.LoginAuth;

/**
 * http请求和相应回来的数据回来的数据
 */
public class HttpData {
	
	private String responseContent ; //相应的数据
	private String ua ;     //浏览器访问的UserAgent认证
	private String cookie ; //账号登录后获取对应的cookie信息
	private String originCookie ; //原始cookie
	private Date expires ;
	private String encode;
	private String host;
	private String connection;
	private String accept;
	private String acceptEncoding;
	private String acceptLanguage;
	private LoginAuth loginAuth;
	private String referer;
	
	public String getReferer() {
		return referer;
	}


	public void setReferer(String referer) {
		this.referer = referer;
	}


	public HttpData() {
		init();
	}


	private void init() {
		this.encode = "UTF-8";
		this.connection="keep-alive";
		this.host="dy.feigua.cn";
		this.accept="application/json, text/javascript, */*; q=0.01";
		this.acceptEncoding="gzip, deflate, br";
		this.acceptLanguage = "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ko;q=0.2,ar;q=0.2,cy;q=0.2";
	}
	
	
	public HttpData( LoginAuth loginAuth ) {
		init();
		this.loginAuth = loginAuth;
		this.ua = this.loginAuth.getUa(); 
		this.cookie =this.loginAuth.getCookie();
	}


	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}





	public LoginAuth getLoginAuth() {
		return loginAuth;
	}



	public void setLoginAuth(LoginAuth loginAuth) {
		this.loginAuth = loginAuth;
	}



	public String getHost() {
		return host;
	}




	public void setHost(String host) {
		this.host = host;
	}




	public String getConnection() {
		return connection;
	}




	public void setConnection(String connection) {
		this.connection = connection;
	}




	public String getAccept() {
		return accept;
	}




	public void setAccept(String accept) {
		this.accept = accept;
	}




	public String getAcceptEncoding() {
		return acceptEncoding;
	}




	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}




	public String getAcceptLanguage() {
		return acceptLanguage;
	}




	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}




	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}
	
	public String getCookies() {
		return cookie;
	}
	public void setCookies(String cookies) {
		this.cookie = cookies;
	}
	public String getUa() {
		return ua;
	}
	public void setUa(String ua) {
		this.ua = ua;
	}




	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getOriginCookie() {
		return originCookie;
	}




	public void setOriginCookie(String originCookie) {
		this.originCookie = originCookie;
	}
	
	
}
