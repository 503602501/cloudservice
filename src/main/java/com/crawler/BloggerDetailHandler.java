package com.crawler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.async.ScheduleTask;
import com.feigua.entity.Constant;
import com.feigua.entity.Environment;
import com.feigua.entity.HttpData;
import com.feigua.entity.LogicException;
import com.feigua.login.LoginService;
import com.mybatisplus.dao.domain.BloggerDetail;
import com.mybatisplus.dao.domain.BloggerVideo;
import com.mybatisplus.dao.domain.HotVideo;
import com.mybatisplus.dao.domain.LoginAuth;
import com.mybatisplus.dao.domain.Task;
import com.mybatisplus.dao.mapper.BloggerDetailMapper;
import com.mybatisplus.dao.mapper.BloggerVideoMapper;
import com.mybatisplus.dao.mapper.HotVideoMapper;
import com.mybatisplus.dao.mapper.LoginAuthMapper;
import com.mybatisplus.dao.mapper.WordTaskMapper;
import com.us.codecraft.XElements;
import com.us.codecraft.Xsoup;
import com.util.EmojiUtil;
import com.util.FilterUtil;
import com.util.HttpUtil;
import com.util.JsonUtil;
import com.util.NumberUtil;
import com.util.StringUtils;

/**
 * @author rocky
 * 博主详情页面的数据
 */
@Component
public class BloggerDetailHandler implements IHandler{
	
	
	private static Logger logger= LoggerFactory.getLogger(BloggerDetailHandler.class);
	
	@Autowired
	private HotVideoMapper hotVideoMapper;
	
    @Autowired
	private BloggerVideoMapper bloggerVideoMapper; 
	
	@Autowired
	private LoginAuthMapper authMapper;
	
	
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private BloggerDetailMapper bloggerDetailMapper;
	
	@Autowired
	private WordTaskMapper wordTaskMapper;
	
	
	public static String removeSpecilChar(String str){
		String result = "";
		if(null != str){
		Pattern pat = Pattern.compile("\\n|\r|\t");
		Matcher mat = pat.matcher(str);
		result = mat.replaceAll("");
		}
		return result;
	}
	 
	

	/**
	 * 同步块
	 * 博主详情的任务获取
	 * @return
	 */
	synchronized private HotVideo queryHotVideo() {
		HotVideo  hotVideo = hotVideoMapper.queryHotVideo();
		if(hotVideo==null){
			return null;
		}
		hotVideo.setStatus(Constant.BUSINESS_DONEING);
		hotVideoMapper.updateByPrimaryKey(hotVideo);
		return hotVideo;
	}
	
	/**
	 * 还原初始化状态
	 * @param hotVideo
	 */
	private void recoveryHotVideo(HotVideo hotVideo ) {
		hotVideo.setStatus(Constant.BUSINESS_INIT);
		hotVideoMapper.updateByPrimaryKey(hotVideo);
	}
	
	@Override
	public void  startWork(LoginAuth loginAuth) throws Exception{
		
		
		/**
		 * 验证是否超过的访问的限制....
		 */
		if( loginAuth.getVisits()>loginAuth.getLimitCount()){
			Environment.removeLoginAuth(loginAuth);
			ScheduleTask.notifyTask(); //唤醒主线程进行处理哈。。。。。。
			delayStop( loginAuth,"超过预设值的访问次数");
			return ;
		}else{
			loginAuth.setVisits(loginAuth.getVisits()+1);
			 
		}
		
		HotVideo hotVideo  = queryHotVideo();
		if(hotVideo==null ){
			Environment.backLoginAuth(loginAuth);
			ScheduleTask.notifyTask(); //唤醒主线程进行处理哈。。。。。。
 			logger.info("************无博主详情的业务****************");
			return ;
		} 
		
		logger.info(Thread.currentThread().getName()+"************博主详情页面的业务爬虫启动****************");
			 
		try {
			HttpData httpData = new HttpData(loginAuth);
			httpData.setReferer( "https://dy.feigua.cn/Member" );
			String url = hotVideo.getBloggerUrl().replace("/Member#", "");
			String content = HttpUtil.getHtmlContent(url,httpData); //getFileText().toString();
			
			if(content==null){
				throw new LogicException("采集到空数据！！！！");
			}
			
			processContent(loginAuth, content);
			
			Document doc = Jsoup.parse(content) ;
			BloggerDetail bloggerDetail= new BloggerDetail();
			bloggerDetail.setCreateDate(new Date());
			bloggerDetail.setBloggerId( HttpUtil.getQueryString(url, "id")) ;
			
			XElements elements =  Xsoup.select(doc, "//div[@class='nickname v-tag']/text()");
			bloggerDetail.setBlogger(EmojiUtil.filterEmoji(elements.get()));
			
			content =removeSpecilChar(content);
			
			String s = FilterUtil.cutString(content,"抖音号：<span>" , "</span>");
			bloggerDetail.setDyAccount(s);
			
			s = FilterUtil.cutString(content,"性别： <span>" , "</span>");
			bloggerDetail.setSex(s.trim());
			  
			s = FilterUtil.cutString(content,"地区：<span>" , "</span>");
	 
			bloggerDetail.setLocation(s);
			
			s = FilterUtil.cutString(content,"年龄：<span>" , "</span>");
			bloggerDetail.setAge(NumberUtil.converInteger(s));
			
			s = FilterUtil.cutString(content,"分类：<span>" , "</span>");
			bloggerDetail.setClassify(s.trim());
			
			s = FilterUtil.cutString(content,"简介：<span>" , "</span>");
			bloggerDetail.setProfile(EmojiUtil.filterEmoji( s )) ;
			  
			elements =  Xsoup.select(doc, "//div[@class='rank-show']/a/text()");
			bloggerDetail.setRank(elements.get()); //排行榜
			 
			
			s = FilterUtil.cutString(content,">粉丝数" , "</span>");
			bloggerDetail.setFans(NumberUtil.converInteger(getWInteger(StringUtils.delHtml( s) ))); //粉丝数量
			
			//作品数
			elements =  Xsoup.select(doc, "//div[@class='col-sm-4 videos-count']/span/text()");
			bloggerDetail.setWorks(NumberUtil.converInteger(elements.get() ));
			
			
			// 总点赞 
			s = FilterUtil.cutString(content,"总点赞 " , "</span>");
			
			bloggerDetail.setSupports(NumberUtil.converInteger(getWInteger(  StringUtils.delHtml(s))  ));
			
			//平均点赞
			s = FilterUtil.cutString(content,"平均点赞" , "</span>");
			bloggerDetail.setAvgSupports(NumberUtil.converInteger( getWInteger(  StringUtils.delHtml(s) ) ));
			
			//集均评论
			s = FilterUtil.cutString(content,"集均评论" , "</span>");
			bloggerDetail.setAvgComments(NumberUtil.converInteger( getWInteger(  StringUtils.delHtml(s)) ));
			
			s = FilterUtil.cutString(content,"集均分享" , "</span>");
			bloggerDetail.setAvgShares(NumberUtil.converInteger( getWInteger(  StringUtils.delHtml(s) )));
			
			
			//最新7天
	    	s = FilterUtil.cutString(content,"最新作品数" , "</div>");
	    	bloggerDetail.setNewWork7( NumberUtil.converInteger( getWInteger( StringUtils.delHtml(s))) );
	    	
	    	
	    	s = FilterUtil.cutString(content,"粉丝增量" , "</div>");
	    	bloggerDetail.setFanAdd7(NumberUtil.converInteger(getWInteger(  StringUtils.delHtml(s))) );
	    	
	    	
	    	s = FilterUtil.cutString(content,"新增点赞" , "</div>");
	    	bloggerDetail.setSuppportAdd7(NumberUtil.converInteger( getWInteger(  StringUtils.delHtml(s)))  );

	    	s = FilterUtil.cutString(content,"新增评论" , "</div>");
	    	bloggerDetail.setCommnetAdd7(NumberUtil.converInteger( getWInteger(  StringUtils.delHtml(s)))  );
	    	
	    	s = FilterUtil.cutString(content,"新增转发" , "</div>");
	    	bloggerDetail.setShareAdd7(NumberUtil.converInteger( getWInteger( StringUtils.delHtml(s)))  );
	    	
	    	//最新30天
//	    	System.out.println("***********30*********");
	    	if(content.split("data-preview js-blogger-overview").length>2){
	    		String thirty =content.split("data-preview js-blogger-overview")[2];
	    		s = FilterUtil.cutString(thirty,"最新作品数" , "</div>");
	    		bloggerDetail.setNewWork30( NumberUtil.converInteger( getWInteger( (StringUtils.delHtml(s)))) );
	    		
	    		s = FilterUtil.cutString(thirty,"粉丝增量" , "</div>");
	    		bloggerDetail.setFanAdd30(NumberUtil.converInteger( getWInteger( StringUtils.delHtml(s))) );
	    		
	    		s = FilterUtil.cutString(thirty,"新增点赞" , "</div>");
	    		bloggerDetail.setSuppportAdd30(NumberUtil.converInteger( getWInteger( StringUtils.delHtml(s))) );
	    		
	    		s = FilterUtil.cutString(thirty,"新增评论" , "</div>");
	    		bloggerDetail.setCommnetAdd30(NumberUtil.converInteger( getWInteger( StringUtils.delHtml(s)) ) );
	    		
	    		s = FilterUtil.cutString(thirty,"新增转发" , "</div>");
	    		bloggerDetail.setShareAdd30(NumberUtil.converInteger( getWInteger( StringUtils.delHtml(s)) ) );
	    	}

	    	/****地区，城市，星座****/
	    	/***************************************************************************************************/
	    	String s1 = FilterUtil.cutString(content,"var bloggerId =" , ";");
			String bloggerId =s1.trim().replace("'", "");
	    	url = "https://dy.feigua.cn/Blogger/FansAnalysis?uid="+bloggerId;
	    	content = HttpUtil.getHtmlContent(url,httpData); //getFileText().toString();
	    	
	    	processContent(loginAuth, content);
	    	
	    	content =removeSpecilChar(content);
			s = FilterUtil.cutString(content,"男" , "</div>");
			bloggerDetail.setManRatio(StringUtils.delHtml(s)  );
			s = FilterUtil.cutString(content,"女" , "</div>");
			bloggerDetail.setWomanRatio(StringUtils.delHtml(s)  );
			
			doc = Jsoup.parse(content)  ; 
			
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[1]/td[1]");
			bloggerDetail.setLocation1(s);
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[1]/td[2]");
			bloggerDetail.setLocation1Ratio(s);
			
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[2]/td[1]");
			bloggerDetail.setLocation2(s);
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[2]/td[2]");
			bloggerDetail.setLocation2Ratio(s);
			
			
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[3]/td[1]");
			bloggerDetail.setLocation3(s);
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[3]/td[2]");
			bloggerDetail.setLocation3Ratio(s);
			
			
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[4]/td[1]");
			bloggerDetail.setLocation4(s);
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[4]/td[2]");
			bloggerDetail.setLocation4Ratio(s);
			
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[5]/td[1]");
			bloggerDetail.setLocation5( s);
			s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[5]/td[2]");
			bloggerDetail.setLocation5Ratio(s );
			
			System.out.println("****************城市如下*************");
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[1]/td[1]");
			bloggerDetail.setCity1( s);
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[1]/td[2]");
			bloggerDetail.setCity1Ratio( s) ;
			
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[2]/td[1]");
			bloggerDetail.setCity2( s);
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[2]/td[2]");
			bloggerDetail.setCity2Ratio( s);
			
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[3]/td[1]");
			bloggerDetail.setCity3(s);
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[3]/td[2]");
			bloggerDetail.setCity3Ratio(s);
			
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[4]/td[1]");
			bloggerDetail.setCity4(s);
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[4]/td[2]");
			bloggerDetail.setCity4Ratio(s);
			
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[5]/td[1]");
			bloggerDetail.setCity5( s);
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[5]/td[2]");
			bloggerDetail.setCity5Ratio(s);
			
			System.out.println("***************星座分布*****************");
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[1]/div[1]");
			bloggerDetail.setZodiac1(s);
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[1]/div[3]");
			bloggerDetail.setZodiac1Ratio(s);
			
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[2]/div[1]");
			bloggerDetail.setZodiac2(s);
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[2]/div[3]");
			bloggerDetail.setZodiac2Ratio(s);
			
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[3]/div[1]");
			bloggerDetail.setZodiac3(s);
			s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[3]/div[3]");
			bloggerDetail.setZodiac3Ratio(s);
			
			
			/***********************处理博主页面的最热视频列表**********************/
			 
			doBloggerVideo(httpData, bloggerDetail);
			
			/***************end********处理博主页面的最热视频列表***end*******************/
			
			bloggerDetailMapper.insert(bloggerDetail);
			
			//更新定时任务的数据状态
			hotVideo.setStatus(Constant.BUSINESS_DONE); //成功处理完成
			
		}catch (LogicException e) {  //可逻辑异常处理，账号反采集等原因无法采集，需要将账号移除，不能继续使用
			logger.error("控制逻辑新增博主明细信息",e);
			//恢复初始化状态
			recoveryHotVideo(hotVideo);
			Environment.removeLoginAuth(loginAuth);
			return ;
		}catch (Exception e) {
			logger.error("异常新增博主明细信息",e);
			e.printStackTrace();
			Environment.backLoginAuth(loginAuth);
			ScheduleTask.notifyTask(); //唤醒主线程进行处理哈。。。。。。
			hotVideo.setStatus(Constant.BUSINESS_EXCEPTION); //3是处理出现异常了
			return ;
		}finally{
			//账号返回队列，以便其他任务获取认证来运行
			hotVideoMapper.updateByPrimaryKey(hotVideo);
		}
		
		//递归自我采集,先休眠10秒
		synchronized (Thread.currentThread()) {
			Thread.currentThread().wait(10000);
		}
		this.startWork(loginAuth);
		
	}



	/**
	 * 处理请求的异常防采集等返回信息
	 * @param loginAuth
	 * @param hotVideo
	 * @param content
	 */
	public void processContent(LoginAuth loginAuth,String content) throws LogicException {
//		logger.info("反采集出现:"+content);
		
		if( content.contains("访问太频繁")){
			delayStop(loginAuth,content);
			logger.info(content);
			throw new LogicException("访问太频繁");
		}
		
		//该账号无法采集了，直接异常吧..，访问的次数超过了每天的量，，，不知道如何处理
		if(content.indexOf("用户权限不足")!=-1 ){
			loginAuth.setStatus(Constant.LOGIN_AUTH_EXCEPTION);
			loginAuth.setRemark(content);
			loginAuth.setUpdateDate(new Date());
			authMapper.updateByPrimaryKey(loginAuth);
			throw new LogicException("用户权限不足");
		}
	}


	/**
	 * 博主详情页面的热门视频，最多前50
	 * @param httpData
	 * @param bloggerDetail
	 * @throws Exception
	 */
	private void doBloggerVideo(HttpData httpData, BloggerDetail bloggerDetail) throws Exception
	  {
		Document doc;
		XElements elements;
		String s;
		BloggerVideo  bloggerVideo = null;
		BloggerVideo  video = null;
		s = getBloggerVideoContent(httpData, bloggerDetail.getBloggerId());
		
		elements =  Xsoup.select("<table>"+s+"<table>", "//tr");
		for (String ss : elements.list()) {
			bloggerVideo = new BloggerVideo();
			doc = Jsoup.parse("<table>"+ss+"</table>");
			 s = Xsoup.selectText(doc, "//div[@class='item-inner']/div[@class='item-title']/a");
			 bloggerVideo.setTitle(EmojiUtil.filterEmoji(s));
			 
			 s = Xsoup.selectText(doc, "//div[@class='item-times']");
			 bloggerVideo.setPublishDate(s.replace("发布时间：", ""));
			 
			 s = Xsoup.select(doc, "//a[@class='source-play']/@href").get();
			 bloggerVideo.setVideoUrl(s);
			 
			 s = Xsoup.selectText(doc, "//div[@class='item-tag clearfix']/ul");
			 bloggerVideo.setHotWord(s);
			 
			 bloggerVideo.setBloggerId(bloggerDetail.getBloggerId());
			 
			 s = Xsoup.selectText(doc, "//span[@class='tag-delete']");
			 if(StringUtils.isEmpty(s)){
				 bloggerVideo.setStatus(1); //有效
			 }else{
				 bloggerVideo.setStatus(0); //隐藏或者删除
			 }
			 
			  ss= removeSpecilChar(ss);
			  s = FilterUtil.cutString(ss,"v-icon-set like" , "<i");
			  s = s.replace("></i>", "");
			  s = s.replace("\"", "");
			  bloggerVideo.setSupports( NumberUtil.converInteger(getWInteger( s)));
			  
			  
			  s = FilterUtil.cutString(ss,"v-icon-set comments" , "<i");
			  s = s.replace("></i>", "");
			  s = s.replace("\"", "");
			  bloggerVideo.setComments( NumberUtil.converInteger(getWInteger( s)));
			  
			  s = FilterUtil.cutString(ss,"v-icon-set reply" , "</div>");
			  s = s.replace("></i>", "");
			  s = s.replace("\"", "");
			  bloggerVideo.setShares( NumberUtil.converInteger(getWInteger( s)));
			  
			  String mid =HttpUtil.getQueryString(bloggerVideo.getVideoUrl() ,"mid");
			  bloggerVideo.setVideoId(FilterUtil.cutString(bloggerVideo.getVideoUrl(), "video/", "/?") +mid );
			  video = bloggerVideoMapper.findByVideoId(bloggerVideo.getVideoId());
			  bloggerVideo.setUpdateDate(new Date());
			  
			  //如果短视频记录已经存在，那就做更新，不存在就插入
			  if(video!=null){
				  bloggerVideo.setId(video.getId());
				  bloggerVideo.setCreateDate( video.getCreateDate());
				  bloggerVideoMapper.updateByPrimaryKey(bloggerVideo);
			  }else{
				  bloggerVideo.setCreateDate(new Date());
				  bloggerVideoMapper.insert(bloggerVideo);
			  }
		}
	}
	
	//https://dy.feigua.cn/Blogger/AwemeList?page=1&id=7481963&shop=0&hide=0&sort=2&fromDate=2019-06-06&toDate=2019-09-04&keyword=;
	public  String getVideourl(Integer page ,String id){
		
		String url = "https://dy.feigua.cn/Blogger/AwemeList?page=%s&id=%s&shop=0&hide=0&sort=2&fromDate=%s&toDate=%s&keyword=&partial=1";
//		System.out.println(url);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String toDate =  sdf.format(new Date());
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -3);
		String fromDate = sdf.format( c.getTime()) ;
		
		return String.format(url, page,id,fromDate,toDate);
		
	}
	
	
	private String getBloggerVideoContent(HttpData httpData,String id ) throws Exception   {
		StringBuffer sb = new StringBuffer();
		String content = null;
		String url =null;
		for (int i = 1; i < 6; i++) {  //爬取5次,每次10个数据
		    url = getVideourl(i, id);
		    content = HttpUtil.getHtmlContent(url,httpData);
			processContent(httpData.getLoginAuth(), content);
			//如果是json格式，就跳出可以了。。。。
			if(JsonUtil.isJSONValid(content)){
				break;
			}
			
			sb.append(content);
			
			synchronized(Thread.currentThread()){
				Thread.currentThread().wait(3000);
			}
		}
		return sb.toString();
	}
	

	public static void main2(String[] args) throws Exception {
		String url = "https://www.douyin.com/share/video/6725360353208749325/?mid=6712667443501730563";
		String s = FilterUtil.cutString(url, "video/", "/?");
		System.out.println(s);
	}
	

	/**
	 * 延迟停止，不要继续采集啦》》》》》》》》
	 * @param hotVideo
	 * @param loginAuth
	 * 
	 */
	private void delayStop( LoginAuth loginAuth,String message) {
		loginAuth.setLastDatetime(new Date());
		loginAuth.setStatus(Constant.LOGIN_AUTH_STOP);
		loginAuth.setRemark(message);
		loginAuth.setUpdateDate(new Date()) ;
		
		int i = authMapper.updateByPrimaryKey(loginAuth);
		
		logger.error("超过了访问的限制....赶紧停止吧"+i);
	}
	
	//转换下数据w
	private  String getWInteger(String s ){
		
		if(s.indexOf("--")!=-1){
			return "0" ;
		}
		
		if(s.indexOf("-")!=-1){
			return "0" ;
		}
		
		if(StringUtils.isEmpty(s)){
			return "0";
		}
		
		if(s.indexOf("w")!=-1){
			Float f = Float.parseFloat(s.replace("w", ""));
			return Math.round(f*10000) +"";
		}else if(s.indexOf("亿")!=-1){
			Float f = Float.parseFloat(s.replace("亿", ""));
			return Math.round(f*100000000) +"";
		}else{
			return s ;
		}
	}

	
}
