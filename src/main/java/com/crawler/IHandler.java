package com.crawler;

import com.mybatisplus.dao.domain.LoginAuth;

public interface IHandler {
	
	void startWork(LoginAuth loginAuth)throws Exception;

}
