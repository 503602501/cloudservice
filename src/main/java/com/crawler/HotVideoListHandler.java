package com.crawler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.async.ScheduleTask;
import com.feigua.entity.Constant;
import com.feigua.entity.Environment;
import com.feigua.entity.HttpData;
import com.feigua.entity.LogicException;
import com.mybatisplus.dao.domain.HotVideo;
import com.mybatisplus.dao.domain.LoginAuth;
import com.mybatisplus.dao.domain.WordTask;
import com.mybatisplus.dao.mapper.HotVideoMapper;
import com.mybatisplus.dao.mapper.WordTaskMapper;
import com.us.codecraft.XElements;
import com.us.codecraft.Xsoup;
import com.util.EmojiUtil;
import com.util.HttpUtil;
import com.util.JsonUtil;
import com.util.StringUtils;

/**
 * @author rocky
 *  热门列表的处理
 */
@Component
public class HotVideoListHandler implements IHandler{
	
	private static Logger logger= LoggerFactory.getLogger(HotVideoListHandler.class);
	
	@Autowired
	private HotVideoMapper hotVideoMapper;
	
	@Autowired
	private WordTaskMapper wordTaskMapper;
	
	
	@Autowired
	BloggerDetailHandler bloggerDetailHandler;
	
	
	/**
	 * 热门列表
	 * @param keyword
	 * @param day
	 * @param sort //0综合排序，1点赞最多，2评论最多，3分享最多
	 * @param page
	 * @return
	 */
	private String getHotVideoUrl(String keyword, Integer day,Integer sort, Integer page) {
		
		String url ="https://dy.feigua.cn/Aweme/Search?keyword="+keyword+"&tag=&likes=0&hours="+day*24+"&duration=0&gender=0&age=0&province=0&city=0&sort="+sort+"&ispromotions=0&page="+page;
		return url;
	}
	

	private String getHotVideoContent(HttpData httpData,String keyword,Integer day) throws Exception {
		String sb = "";
		for (int i = 1; i < 7; i++) {
			String url = getHotVideoUrl(keyword,day, 1, i);
			String content = HttpUtil.getHtmlContent(url,httpData);
			bloggerDetailHandler.processContent(httpData.getLoginAuth(), content);
			if(i==1){
				sb= content;
			}else if(sb.lastIndexOf("</tr>")!=-1){
				//插入到最后的一个</tr>后面
				sb = sb.substring(0, sb.lastIndexOf("</tr>")+5)+content +sb.substring(sb.lastIndexOf("</tr>")+5);
			}else{
				logger.error("为啥会没有内容！！！"+sb);
			}
			//如果不存在“正在加载，说明没有分页数据，直接退出”
			if(JsonUtil.isJSONValid(content)){
				break;
			}
			
			synchronized(Thread.currentThread()){
				Thread.currentThread().wait(3000);
				System.out.println("自动唤醒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			}
		}
		return sb.toString();
	}

	/**
	 * 防止多线程的同步块
	 * @return
	 */
	synchronized private WordTask   selectWordTask() {
		WordTask wordTask = wordTaskMapper.selectWordTask();
		if(wordTask==null){
			return null;
		}
		wordTask.setStatus(2) ;
		wordTaskMapper.updateByPrimaryKey(wordTask);
		return wordTask;
	}
	
	@Override
	public void  startWork(LoginAuth loginAuth) throws Exception{
		
		//自己取出任务
		WordTask wordTask = selectWordTask();
		
		if(wordTask==null){
			Environment.backLoginAuth(loginAuth);
			ScheduleTask.notifyTask(); //唤醒主线程进行处理哈。。。。。。
			logger.info("************无热词列表的业务****************");
			return ;
		}
		
		logger.info(Thread.currentThread().getName()+"**********热词列表的业务爬虫启动*************");
		
		HttpData httpData = new HttpData(loginAuth);
		
		try {
		 
			List<Integer> list =Arrays.asList(1,3,7,15,30);
			for (Integer day : list) {
				handleData(wordTask, loginAuth, day, httpData);  //处理数据
				synchronized (Thread.currentThread()) {
					Thread.currentThread().wait(5000);
				}
			}
			
			//排位50 算法排序后，处理掉一批无需运行的博主信息
			hotVideoMapper.updateSortBlogger(wordTask.getId());
			
			//排位50后的2状态，处理成4状态,表示无需处理
			hotVideoMapper.updateBloggerNotNeed(wordTask.getId());
			
			//更新定时任务的数据状态
			wordTask.setStatus(1);
			wordTask.setUpdateDate(new Date());
			
		}catch (LogicException e) {
			logger.error("可控关键词热门列表异常",e);
			wordTask.setStatus(0);  //恢复原始待运行状态
			Environment.removeLoginAuth(loginAuth);
			return ;
		} 
		catch (Exception e) {
			logger.error("关键词热门列表更新失败",e);
			e.printStackTrace();
			ScheduleTask.notifyTask(); //唤醒主线程进行处理哈。。。。。。
			Environment.backLoginAuth(loginAuth);
			wordTask.setStatus(3);
			return ;
		}finally{
			//账号返回队列，以便其他任务获取认证来运行
			wordTaskMapper.updateByPrimaryKey(wordTask);
		}
		
		//递归自我采集,先休眠10秒
		synchronized (Thread.currentThread()) {
			Thread.currentThread().wait(10000);
		}
		this.startWork(loginAuth);
	}


	private void handleData(WordTask wordTask, LoginAuth loginAuth, Integer day, HttpData httpData) throws Exception {
		
		String content = getHotVideoContent(httpData,wordTask.getKeyword(),day);
		
		XElements elements =  Xsoup.select(content, "//tr");
		HotVideo  hotVideo = null;
		Element doc =null;
		Elements element=elements.getElements();
		String bloggerUrl = "";
		String time = "";
		for (int i = 1; i < element.size(); i++) {
			hotVideo = new HotVideo();
			hotVideo.setAccount(loginAuth.getAccount());
			doc = Jsoup.parse("<table>"+element.get(i).html()+"</table>");
			hotVideo.setHotWord(Xsoup.selectText(doc, "//td"));
			hotVideo.setTitle(EmojiUtil.filterEmoji( Xsoup.selectText(doc, "//div[@class='item-title']") ));
			hotVideo.setHotWord(  Xsoup.selectText(doc, "//div[@class='item-tag clearfix']/ul") );
			hotVideo.setBlogger(EmojiUtil.filterEmoji(  Xsoup.select(doc, "//table/tbody/tr/td[3]/div/div/div/a/text()").get()) );  //这里不对
			hotVideo.setBloggerUrl(Xsoup.selectText(doc, "//div[@class='item-title']/a"));
			hotVideo.setCycle(day+"");
			bloggerUrl = Xsoup.select(doc, "//table/tbody/tr/td[3]/div/div/div/a/@href").get();
			hotVideo.setBloggerUrl("https://dy.feigua.cn/Member#"+bloggerUrl.substring(1));
			hotVideo.setBloggerId(HttpUtil.getQueryString(hotVideo.getBloggerUrl(), "id"));
			time =  Xsoup.selectText(doc, "//div[@class='item-times']").replace("视频时长：", "")   ;
			hotVideo.setTime( time.substring(0,time.indexOf("秒")) );
			hotVideo.setStatus(Constant.BUSINESS_DONEING);
			hotVideo.setSupports(getWInteger( Xsoup.select(doc, "//table/tbody/tr/td[4]/text()").get())  );
			hotVideo.setComments( getWInteger(Xsoup.select(doc, "//table/tbody/tr/td[5]/text()").get() ) );
			hotVideo.setVideoUrl( Xsoup.select(doc, "//table/tbody/tr/td[6]/div/a[@class='source-play']/@href").get() );
			hotVideo.setCreateDate(new Date());
			hotVideo.setTaskId(wordTask.getId());
			
			//清理重复数据为已经处理 4，无需处理
			hotVideoMapper.updateRepeat(hotVideo.getBloggerId());
			hotVideoMapper.insert(hotVideo);
		}
	}
	
	//转换下数据w
	private  String getWInteger(String s ){
		if(StringUtils.isEmpty(s)){
			return "0";
		}
		
		if(s.indexOf("w")!=-1){
			Float f = Float.parseFloat(s.replace("w", ""));
			return Math.round(f*10000) +"";
		}else{
			return s ;
		}
	}

	
	public static void main2(String[] args) throws Exception {
		HttpData data = new HttpData(new LoginAuth());
		System.out.println("sd");
	}
 
	
}
