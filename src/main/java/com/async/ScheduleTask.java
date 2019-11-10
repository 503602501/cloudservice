package com.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crawler.BloggerDetailHandler;
import com.crawler.HotVideoListHandler;
import com.feigua.entity.Environment;
import com.mybatisplus.dao.domain.HotVideo;
import com.mybatisplus.dao.domain.LoginAuth;
import com.mybatisplus.dao.domain.WordTask;
import com.mybatisplus.dao.mapper.BloggerDetailMapper;
import com.mybatisplus.dao.mapper.HotVideoMapper;
import com.mybatisplus.dao.mapper.WordTaskMapper;

/**
 * 采集任务分配的定时器
 * @author rocky
 *
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ScheduleTask {

	
	private static Logger logger= LoggerFactory.getLogger(ScheduleTask.class);
	
	private volatile static boolean isWake = false;   //是否被唤醒
	
	@Autowired
	private BloggerDetailHandler boggerDetailHandler;
	
	
	
	@Autowired
	private HotVideoListHandler hotVideoListHandler;
	
	@Autowired
	private WordTaskMapper wordTaskMapper ;
	
	@Autowired
	BloggerDetailMapper bloggerDetailMapper;
	
	
	@Autowired
	private HotVideoMapper hotVideoMapper;
	
	
	@Autowired
	private AsyncTaskService asyncTaskService;
	
	@Autowired
	Environment environment ;
	
	/**
	 * 等待
	 * @param time
	 */
	private static void  waitTask(Integer time){
		synchronized (ScheduleTask.class) {
			try {
				ScheduleTask.class.wait(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 唤醒
	 * @param time
	 */
	public static void  notifyTask(){
		synchronized (ScheduleTask.class) {
			isWake = true;
			ScheduleTask.class.notifyAll();
		}
	}
	
	
	/**
	 * 定时任务，分配账号和任务
	 * 1.判断登录账号是否为空
	 * 2.判断是否存在需要处理的关键词
	 * 3.获取一个活跃的账号，并且分发关键词的采集任务
     * 4.提交任务，异步执行关键词的采集任务
     */
//    @Scheduled(initialDelay=1000,fixedDelay=60000)
//    @Scheduled(cron="0 0/1 18,23 * * ?")
//	@Scheduled(cron="0 0/10 18-23 * * ? ")  //晚上10点到凌晨2点之间，每隔10分钟跑一次，如果还没跑完就隔10分钟后再跑
	@Scheduled(cron="0 0/10 0,1,2,21,22,23 * * ? ")  //晚上10点到凌晨2点之间，每隔10分钟跑一次，如果还没跑完就隔10分钟后再跑
    public void task() {
    	
    	this.isWake= false;
    	logger.info("***************************定时任务启动***************************");
    	
    	/*if(isWake==false){
    		return ;
    	}*/
    	
    	//账号队列不为空
    	LoginAuth loginAuth = environment.getActiveLoginAuth();
    	 
		//查询一下是否可以拿出库中可以运行的账号
		if(loginAuth==null){
			logger.info("无可以运行的账号提取来运行任务,休眠10分钟，中途可以被唤醒");
			waitTask(600000);
			if(isWake){
				this.task(); //递归
			}
			return ;
		}
    	
		//关键词的采集任务
		WordTask wordTask = wordTaskMapper.selectWordTask();
    	if(wordTask!=null){ //启动关键词的列表采集线程
    		asyncTaskService.executeAsyncTask( hotVideoListHandler,loginAuth);
    		loginAuth = environment.getActiveLoginAuth();
    	}
    	
    	if(loginAuth==null){
			logger.info("无可以运行的账号提取来运行任务,休眠10分钟，中途可以被唤醒");
			waitTask(600000);
			if(isWake){
				this.task(); //被唤醒才是递归
			}
			return ;
		}
    	
    	//博主详情采集
    	HotVideo hotVideo = hotVideoMapper.queryHotVideo();
    	if(hotVideo!=null){
    		asyncTaskService.executeAsyncTask( boggerDetailHandler,loginAuth);
    	}else if(loginAuth!=null){
    		Environment.backLoginAuth(loginAuth);
    	}
    }
	
}
