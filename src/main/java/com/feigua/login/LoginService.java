package com.feigua.login;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.LoginApplicationListener;
import com.feigua.entity.Constant;
import com.feigua.entity.Environment;
import com.feigua.entity.HttpData;
import com.mybatisplus.dao.domain.LoginAuth;
import com.mybatisplus.dao.mapper.LoginAuthMapper;
import com.util.HttpUtil;
import com.util.JsonUtil;

/**
 * 用户登录服务
 */
@Component
public class LoginService {

	
	private static Logger logger= LoggerFactory.getLogger(LoginService.class);
	@Autowired
	private LoginAuthMapper authMapper;
	
	
	@Autowired
	LoginApplicationListener loginValidateHandler;

	/**
	 * 登录一个账号
	 * @return
	 */
	public HttpData login(LoginAuth loginAuth  ) {
		// 账号登录，保存数据库，返回cookie
		String url = "https://dy.feigua.cn/Login/Login";
		HashMap<String, String> params = new HashMap<>();
		params.put("tel", loginAuth.getAccount());
		params.put("pwd", loginAuth.getPassword());
		try {
			HttpData  httpData =  HttpUtil.getDataByPost(url, params);
			
			loginAuth.setUpdateDate(new Date());
			loginAuth.setRemark("");
			loginAuth.setVisits(0);
			//登录异常
			if(httpData==null){
				loginAuth.setStatus(Constant.LOGIN_AUTH_EXCEPTION);
				loginAuth.setRemark("无法登录成功");
				authMapper.updateByPrimaryKeySelective(loginAuth);
				Environment.removeLoginAuth(loginAuth);
				
				return null;
			}else{
				loginAuth.setStatus(Constant.LOGIN_AUTH_ACTIVE);
				loginAuth.setCreateDate(new Date());
				loginAuth.setOriginCookie(httpData.getOriginCookie());
				loginAuth.setCookie(httpData.getCookies());
				loginAuth.setExpires(httpData.getExpires());
				loginAuth.setUa(httpData.getUa());
				loginAuth.setContent(httpData.getResponseContent());
				authMapper.updateByPrimaryKeySelective(loginAuth);
				
				//添加登录认证到队列里
				Environment.backLoginAuth(loginAuth);
//				HttpConsumer.setAccountMap(loginAuth.getAccount(), loginAuth);
//				HttpConsumer.setCookieMap(loginAuth.getCookie(), loginAuth);
				
				return httpData;
			}
		} catch (Exception e) {
			//系统异常情况的登录
			Environment.removeLoginAuth(loginAuth);
			logger.info("登录失败：",e);
			System.out.println("登录失败！！！！！！！！！！"+loginAuth.getAccount());
			e.printStackTrace();
			String message = e.getMessage() ;
			if(message.length()>255){
				message = message.substring(0, 254);
			}
			loginAuth.setRemark(message);
			loginAuth.setStatus(Constant.LOGIN_AUTH_EXCEPTION);
			authMapper.updateByPrimaryKey(loginAuth);
		}
		
		return null;
	}
	
	/**
	 * 登录所有的账号
	 * 
	 */
	public void loginAccount(Map<String, Integer> params) {
		 List<LoginAuth> list= authMapper.selectList(params);
		 logger.info("提取出多少个："+list.size());
		 for (LoginAuth loginAuth : list) {
			 processLogin(loginAuth);
			 logger.info(loginAuth.getAccount()+"登录后:"+Environment.freeLoginAuthQueue.size() ) ;
		 }
	}

	/**
	 * 无参数，登录所有的账号
	 */
	public void loginAccount(){
		loginAccount(null);
	}
	/**
	 * 
	 * @param loginAuth
	 * @return true:验证当前的账号是可用，false当前账号不可用需要做处理
 	 */
	public boolean  processLogin(LoginAuth loginAuth) {
		 //未初始化，就登录一下啦
		 if( Constant.LOGIN_AUTH_INIT.equals(loginAuth.getStatus())){ 
			 login(loginAuth);
			return false;
		 }
		 
		 //过期的也需要重新登录一下
		 if( Constant.LOGIN_AUTH_EXPIRE.equals( loginAuth.getStatus())){
			 Environment.removeLoginAuth(loginAuth);
			 login(loginAuth);
			 return false;
		 }
		 
		 //活跃的，需要检查下是否cookie过期，如果过期了需要设置
		 if( Constant.LOGIN_AUTH_ACTIVE.equals( loginAuth.getStatus()) || Constant.LOGIN_AUTH_STOP.equals( loginAuth.getStatus())){
			 
			 Date date = new Date();
			 boolean bool = validateHeartBeat(loginAuth); //通过心跳测试，发现已经失效了。。。。。
			 if( date.after(loginAuth.getExpires()) || !bool ){  //是否登录过期了
				 loginAuth.setStatus(Constant.LOGIN_AUTH_EXPIRE);
				 loginAuth.setUpdateDate(new Date());
				 Environment.removeLoginAuth(loginAuth);
				 authMapper.updateByPrimaryKey(loginAuth);
				 login(loginAuth); //过期的账号重新登录啦
				 return false;
			 }else{
				 //账号验证没问题，那就放到空闲的队列，等待其他其他线程获取 ; 如果是延迟停止的账号，修改位活跃状态哈！！！！
				 if(Constant.LOGIN_AUTH_STOP.equals( loginAuth.getStatus() )){
					 loginAuth.setStatus(Constant.LOGIN_AUTH_ACTIVE);
					 loginAuth.setRemark("");
					 loginAuth.setVisits(0);
					 authMapper.updateByPrimaryKey(loginAuth);
				 }
				 Environment.backLoginAuth(loginAuth);
				 return true;
			 }
		 }
		 
		 return false;
	}
	
	/**
	 * 登录过期的账号
	 * 检查账号是否过期，如果过期就做登录认证
	 * @return
	 * @throws InterruptedException 
	 */
	/*public LoginAuth validateLogin(LoginAuth loginAuth) throws InterruptedException {
		
		 //需要检查一下活跃的账号是否cookie过期了
		 Date date = new Date();
		 if( date.after(loginAuth.getExpires())){  //是否登录过期了
			 loginAuth = asyncLoginTask();
		 }else{
			 System.out.println("************");
		 }
		 
		return loginAuth ;
		
	}*/
	
	
	
	
	/**
	 * 验证心跳是否正常来判断登录信息是否可以继续使用
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean validateHeartBeat(LoginAuth loginAuth)  {
		String url = "https://dy.feigua.cn/User/Heartbeat";
		HttpData httpData = new HttpData(loginAuth);
		httpData.setReferer( "https://dy.feigua.cn/Member" );
		String status = "";
		try {
			String content = HttpUtil.getHtmlContent(url,httpData);
			boolean bool = JsonUtil.isJSONValid(content);
			if(bool){
				status = JsonUtil.getJSONValue("status", content);
			}else{
				logger.error("心跳失败："+content);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return !"".equals(status) ? true : false;
	}
	
	/**
	 * 从数据库中一个登录的信息
	 * 1.如果没有登录的信息，则重新登录，然后才返回有等信息的实体
	 * @return
	 * @throws InterruptedException 
	 */
	/*public LoginAuth getLoginAuth() throws InterruptedException {
		LoginAuth  loginAuth = authMapper.queryLoginActive();
		if(loginAuth==null){  
			System.out.println("查询不到在线的登录认证......开始等待。。。。。。。");
			loginAuth = asyncLoginTask();
		}else{ 
			
		}
		
		return loginAuth;
	}
	*/
	
	/**
	 * 异步登录线程,返回登录的已经登录的认证信息
	 * @return
	 * @throws InterruptedException
	 */
	/*private LoginAuth asyncLoginTask() throws InterruptedException {
		Object lock= Environment.LOGIN_LOCK;
		loginValidateHandler.asyncTask();
		synchronized (lock) {  //等待
			lock.wait();
		}
		System.out.println("被唤醒。。。。。。。");
		return  authMapper.queryLoginActive();
	}*/

}
