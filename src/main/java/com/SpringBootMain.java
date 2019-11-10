package com;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@MapperScan("com.mybatisplus.dao.mapper")
public class SpringBootMain {
	
	private static Logger logger= LoggerFactory.getLogger(SpringBootMain.class);
	
	
	@RequestMapping("/test")
	public String  test() {
		return "success";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootMain.class, args);
	}

}
