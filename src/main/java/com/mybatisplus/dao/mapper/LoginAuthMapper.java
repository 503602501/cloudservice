package com.mybatisplus.dao.mapper;

import java.util.List;
import java.util.Map;

import com.mybatisplus.dao.domain.LoginAuth;
import tk.mybatis.mapper.common.Mapper;

public interface LoginAuthMapper extends Mapper<LoginAuth> {

	LoginAuth queryAuth();
	
	LoginAuth queryLoginActive();

	List<LoginAuth> selectList(Map<String, Integer> params);
}