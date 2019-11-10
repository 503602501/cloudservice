package com.feigua.entity;

import java.util.concurrent.LinkedBlockingQueue;

public class UserAgent {
	
	private static LinkedBlockingQueue<String> uaQueue ;
	static{
		uaQueue= new LinkedBlockingQueue<>();
		uaQueue.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		uaQueue.add("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.29 Safari/537.36");
		uaQueue.add("Mozilla/4.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.71.30 Safari/537.36");
	}
	public static String getUa() {
		return uaQueue.poll();
	}
	public static void setUa(String ua) {
		uaQueue.add(ua);
	}
	
}
