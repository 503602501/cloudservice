package com.mybatisplus.dao.mapper;

import com.mybatisplus.dao.domain.WordTask;
import tk.mybatis.mapper.common.Mapper;

public interface WordTaskMapper extends Mapper<WordTask> {

	WordTask selectWordTask();
	
}