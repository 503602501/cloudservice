package com.mybatisplus.dao.mapper;

import tk.mybatis.mapper.common.Mapper;

import com.mybatisplus.dao.domain.HotVideo;

public interface HotVideoMapper extends Mapper<HotVideo> {
	
	int updateRepeat(String bloggerId );
	
	HotVideo queryHotVideo( );

	void updateSortBlogger(Integer taskId);

	void updateBloggerNotNeed(Integer taskId);
}