package threadtest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ThreadMain {

	
	public static void main(String[] args) {
		
		
		   ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
	        taskExecutor.setCorePoolSize(3);// 最小线程数,最多可以达到3天线程同时运行
	        taskExecutor.setMaxPoolSize(10);// 最大线程数
	        taskExecutor.setQueueCapacity(2500);// 等待队列
	        taskExecutor.setKeepAliveSeconds(300); //线程5分钟都没有任务到达，可以销毁
	        taskExecutor.initialize();

	        for (int j = 0; j < 3; j++) {
	        	SpringThread t = new SpringThread(j);
	        	taskExecutor.execute(t);
			}
	        
//	        taskExecutor.shutdown();
	        
	}
}
