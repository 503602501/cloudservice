package com.async;

import java.util.Random;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.HotVideoListHandler;
import com.crawler.IHandler;
import com.mybatisplus.dao.domain.LoginAuth;
import com.mybatisplus.dao.domain.Task;

@Component
public class AsyncTaskService {
	
	private Random random = new Random();// 默认构造方法

	private static Logger logger= LoggerFactory.getLogger(HotVideoListHandler.class);
	
	
	/**
	 * 无返回值 ; 表明是异步方法
	 * @param iHandler
	 * @param task
	 */
	@Async
	public void executeAsyncTask(IHandler iHandler,LoginAuth loginAuth) {
		logger.info(Thread.currentThread().getName() + "异步线程执行**************");
		try {
			/*if(! task.isCanRun() ){
				logger.info( System.currentTimeMillis()+ "###############未能运行，等待毫秒"+task.getWaitTime() );
				synchronized(Thread.currentThread()){
					Thread.currentThread().wait( task.getWaitTime() );
				}
				logger.info(  "###############等待完毕"+System.currentTimeMillis() );
			}*/
			
			iHandler.startWork(loginAuth);
			
		} catch (Exception e) {
			logger.error("异步线程执行失败",e);
			e.printStackTrace();
		}
		
		logger.info(Thread.currentThread().getName()+ "异步线程执行结束**************");
		
		
	}

	/**
	 * 异常调用返回Future
	 * 
	 * @param i
	 * @return
	 * @throws InterruptedException
	 */
	@Async
	public Future<String> asyncInvokeReturnFuture(int i)
			throws InterruptedException {
		System.out.println("input is " + i);
		Thread.sleep(1000 * random.nextInt(i));

		Future<String> future = new AsyncResult<String>("success:" + i);// Future接收返回值，这里是String类型，可以指明其他类型

		return future;
	}

}
