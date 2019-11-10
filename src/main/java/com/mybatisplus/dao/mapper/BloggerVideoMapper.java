package com.mybatisplus.dao.mapper;

import com.mybatisplus.dao.domain.BloggerVideo;
import tk.mybatis.mapper.common.Mapper;

public interface BloggerVideoMapper extends Mapper<BloggerVideo> {

	BloggerVideo findByVideoId(String videoId);
}