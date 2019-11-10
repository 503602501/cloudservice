package com.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feigua.entity.Environment;
import com.feigua.entity.HttpData;
import com.feigua.entity.UserAgent;
import com.google.gson.JsonObject;
import com.mybatisplus.dao.domain.LoginAuth;



public class HttpUtil {

	private static int socketTimeout = 10000;  //读超时时间（等待数据时间）
	private static int connectTimeout = 10000; //连接超时时间  
	private static int connectionRequestTimeout = 10000;  //从连接池中获取连接的等待时间
	private static int maxConnTotal = 300;   //最大不要超过1000  
	private static int maxConnPerRoute = 200;//路由的实际的单个连接池大小，如tps定为50，那就配置50  
	private static String UA="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36";
	private static final Logger logger =  LoggerFactory.getLogger(HttpUtil.class);
	private static 	CloseableHttpClient httpclient ;
	private static SSLContext sslContext ;
	static{
		
/*	 	*//*******第一种https验证******//*
		 try {
			 sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {

				@Override
				public boolean isTrusted( java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
					return true;
				}
			 
			   }).build();
//			 sslContext = SSLContext.getInstance("TLS");
			 sslContext = SSLContext.getInstance("SSL");
		} catch (Exception e1) {
			e1.printStackTrace();
		} */
			sslContext = ImageUtil.createIgnoreVerifySSL();
		
			  
//		HttpHost proxy=new HttpHost("163.204.242.74", 9999);
			  
		RequestConfig config = RequestConfig.custom() //.setProxy(proxy)
		         .setSocketTimeout(socketTimeout)  
		         .setConnectTimeout(connectTimeout)  
		         .setConnectionRequestTimeout(connectionRequestTimeout).build();  
		httpclient = HttpClients.custom().setDefaultRequestConfig(config)  
		         .setMaxConnTotal(maxConnTotal)  
		         .setSslcontext(sslContext)
		         .setMaxConnPerRoute(maxConnPerRoute).build();  
//		httpclient.getConnectionManager().shutdown(); 未做关闭不知道是否有问题
	}
	
	
	public static String getHtmlContent(String httpPath) throws Exception {
		 return getHtmlContent( httpPath,new HttpData());
	}
	
	public static String getHtmlContent(String httpPath,String cookies) throws Exception {
		HttpData data = new HttpData();
		data.setCookies(cookies);
		return getHtmlContent( httpPath,data);
	}
	
	

	/*URIBuilder uriBuilder = new URIBuilder(httpPath);
	List<NameValuePair> list = new LinkedList<>();
    BasicNameValuePair param1 = new BasicNameValuePair("cUserIds", "12506545");
    list.add(param1);
    uriBuilder.setParameters(list);*/
    /*URIBuilder uriBuilder = new URIBuilder(httpPath);
	List<NameValuePair> list = new LinkedList<>();
    BasicNameValuePair param1 = new BasicNameValuePair("skuId", "13308189032");
    BasicNameValuePair param2 = new BasicNameValuePair("cat", "5025,5026,13674");
    BasicNameValuePair param3 = new BasicNameValuePair("venderId", "214561");

    BasicNameValuePair param4 = new BasicNameValuePair("area", "1_2800_55812_1");
    BasicNameValuePair param5 = new BasicNameValuePair("buyNum", "1");
    BasicNameValuePair param6 = new BasicNameValuePair("extraParam", "{\"originid\":'1'}");
    list.add(param1);
    list.add(param2);
    list.add(param3);
    list.add(param4);
//    list.add(param5);
    list.add(param6);
    uriBuilder.setParameters(list);
*/
    // 根据带参数的URI对象构建GET请求对象
//    HttpGet httpGet = new HttpGet(uriBuilder.build()); 
	
	/**
	 * 
	 * 描述:获取html的内容片段
	 * 
	 * @param httpPath
	 *            :请求路径
	 * @return
	 * @throws Exception
	 */
	public static String getHtmlContent(String httpPath,HttpData data) throws Exception {

		
		String charset = data.getEncode();
		
		if(StringUtils.isEmpty(httpPath)){
			return "";
		}
		SslUtils.ignoreSsl();
        
		// 一般爬虫请求都用Get，Get请求在HTTP请求协议里代表安全的查看:这个请求对象里可以添加http的请求头等
		HttpGet httpGet = new HttpGet(httpPath.trim());
		
		if(!StringUtils.isEmpty(data.getHost())){
			httpGet.setHeader("Host", data.getHost());
		}
		
		if(!StringUtils.isEmpty(data.getConnection())){
			httpGet.setHeader("Connection", data.getConnection());
		}
		
		if(!StringUtils.isEmpty(data.getAccept())){
			httpGet.setHeader("Accept", data.getAccept());
		}
		
		if(!StringUtils.isEmpty(data.getAcceptEncoding())){
			httpGet.setHeader("Accept-Encoding", data.getAcceptEncoding());
		}
		
		if(!StringUtils.isEmpty(data.getAcceptLanguage())){
			httpGet.setHeader("Accept-Language", data.getAcceptLanguage());
		}
		
		// 设置Get请求头的 User-Agent (模拟代理浏览器信息)
		httpGet.setHeader( "User-Agent", data.getUa());
		if(StringUtils.isEmpty(data.getReferer())){
			httpGet.setHeader( "Referer", data.getReferer());
		}
		if(!StringUtils.isEmpty(data.getCookies())){
			httpGet.setHeader("Cookie",data.getCookies());
		}
		
		// 用浏览器模拟对象httpClient，发送一个Get请求:可以通过这个响应对象获得很多http的响应信息
		InputStream is = null;
		FileOutputStream outputStream = null;
		CloseableHttpResponse respond = null;
		String result = null;
		try {
			respond = httpclient.execute(httpGet);
//			respond.setHeader("Connection", "close");
			if (HttpStatus.SC_OK != respond.getStatusLine().getStatusCode()) {
				httpGet.abort();
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
			// 获取返回的网页实体
			HttpEntity entity = respond.getEntity();
			if (entity != null) {
				is = entity.getContent();
				
			/*	Header[] headers = respond.getAllHeaders();  
				
		        for(Header h:headers){  
		          System.out.println("输出："+h);  
		        } */
		        
		        result = IOUtils.toString(is, charset);
		      
		        return result ; 
			} else {
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("请求异常：" + httpPath + "|" + e.getMessage(), e);
		} finally {
			if(data.getLoginAuth()!=null){
				data.getLoginAuth().setLastTime(System.currentTimeMillis());
			}
			closeIO(httpPath, is, outputStream, respond);
		}
		return null;
	}
	
	/**
	 * 无需cookies
	 * @param httpPath
	 * @param urlParameters
	 * @return
	 * @throws Exception
	 */
	public static String postHtmlContent(String httpPath,List<NameValuePair> urlParameters) throws Exception {
		 return postHtmlContent(  httpPath,   urlParameters,  "") ;
	}
	
	/**
	 * 
	 * @param httpPath
	 * @param params  需要提交的参数
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public static String postHtmlContent(String httpPath,HashMap<String,String> params ,String cookies) throws Exception {
		
		List<NameValuePair> list = convertPair(params);
		
		return postHtmlContent(httpPath, list, cookies);
		
	}
	
	/**
	 * 
	 * @param httpPath
	 * @param params  需要提交的参数
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	
	public static String getHtmlContent(String httpPath,HashMap<String,String> params ,String refer,boolean changeUa) throws Exception {
		
		List<NameValuePair> list = convertPair(params);
		
		return getHtmlContent(httpPath, list, refer,changeUa);
		
	}

	private static List<NameValuePair> convertPair(HashMap<String, String> params) {
		List<NameValuePair> list = new LinkedList<>();
		
		for(Map.Entry<String, String> entry : params.entrySet()){
			String mapKey = entry.getKey();
			String mapValue = entry.getValue();
			BasicNameValuePair param = new BasicNameValuePair(mapKey, mapValue);
			list.add(param);
		}
		return list;
	}
	
	/**
	 * 描述:获取html的内容片段
	 * 
	 * @param httpPath
	 *            :请求路径
	 * @return
	 * @throws Exception
	 */
	public static String postHtmlContent(String httpPath,List<NameValuePair> urlParameters,String cookies) throws Exception {
		
		// 一般爬虫请求都用Get，Get请求在HTTP请求协议里代表安全的查看:这个请求对象里可以添加http的请求头等
		HttpPost httpPost= new HttpPost(httpPath.trim());
		
		// 设置Get请求头的 User-Agent (模拟代理浏览器信息)
		httpPost.setHeader( "User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		httpPost.setHeader("Cookie",cookies);
		
//		httpPost.setHeader("Referer", "http://www.dataoke.com/qlist/?px=zh&tm_jpmj=tm&xlqj=10000&page=1");
		// 用浏览器模拟对象httpClient，发送一个Get请求:可以通过这个响应对象获得很多http的响应信息
		InputStream is = null;
		FileOutputStream outputStream = null;
		CloseableHttpResponse respond = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(urlParameters,Consts.UTF_8));
			respond = httpclient.execute(httpPost);
			respond.setHeader("Connection", "close");
			if (HttpStatus.SC_OK != respond.getStatusLine().getStatusCode()) {
				httpPost.abort();
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
			// 获取返回的网页实体
			HttpEntity entity = respond.getEntity();
			if (entity != null) {
				is = entity.getContent();
				return IOUtils.toString(is, "UTF-8");
			} else {
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
		} catch (Exception e) {
			logger.error("请求异常：" + httpPath + "," + e.getMessage(), e);
		} finally {
			closeIO(httpPath, is, outputStream, respond);
		}
		return null;
	}
	
	/**
	 * 描述:post获取登录cookies
	 * 
	 * @param httpPath
	 *            :请求路径
	 * @return
	 * @throws Exception
	 */
	public static HttpData getDataByPost(String httpPath,HashMap<String, String> params) throws Exception {
		
		
		List<NameValuePair> urlParameters= convertPair(params);
		String ua =  UserAgent.getUa() ;
		// 一般爬虫请求都用Get，Get请求在HTTP请求协议里代表安全的查看:这个请求对象里可以添加http的请求头等
		HttpPost httpPost= new HttpPost(httpPath.trim());
		
		// 设置Get请求头的 User-Agent (模拟代理浏览器信息)
		httpPost.setHeader( "User-Agent", ua);
		httpPost.setHeader("Referer", "https://dy.feigua.cn");
		httpPost.setHeader("Host", "dy.feigua.cn");
		httpPost.setHeader("Origin", "https://dy.feigua.cn");
//		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ko;q=0.2,ar;q=0.2,cy;q=0.2");
		httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
		// 用浏览器模拟对象httpClient，发送一个Get请求:可以通过这个响应对象获得很多http的响应信息
		InputStream is = null;
		FileOutputStream outputStream = null;
		CloseableHttpResponse respond = null;
		HttpData httpData =null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(urlParameters,Consts.UTF_8));
			respond = httpclient.execute(httpPost);
//			respond.setHeader("Connection", "close");
			if (HttpStatus.SC_OK != respond.getStatusLine().getStatusCode()) {
				httpPost.abort();
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
			// 获取返回的网页实体
			HttpEntity entity = respond.getEntity();
			if (entity != null) {
				httpData = new HttpData();
				is = entity.getContent();
				httpData.setResponseContent(IOUtils.toString(is, "UTF-8"));
			} else {
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
			
			Header[] headers = respond.getAllHeaders();  
			StringBuffer sb = new StringBuffer();
			StringBuffer originCookie = new StringBuffer();
	        for(Header h:headers){
	        	originCookie.append(h.getName()+"="+h.getValue()+";");
	        	System.out.println();
	        	if("Set-Cookie".equals(h.getName())){
	        		String[] s = h.getValue().split(";");
	        		sb.append(s[0]+";");
	        		if(s.length>1 && s[2].indexOf("expires")!=-1){
	        			String s1 = s[2].replace("expires=", "")+" +0800";
	        			httpData.setExpires(new Date(s1));
	        		}
	        	}
	        }
	        
			httpData.setUa(ua);
			httpData.setCookies(sb.toString());
			httpData.setOriginCookie(originCookie.toString());
			return httpData;
		} catch (Exception e) {
			logger.error("请求异常：" + httpPath + "," + e.getMessage(), e);
		} finally {
			closeIO(httpPath, is, outputStream, respond);
		}
		return null;
	}
	
	

	private static void closeIO(String httpPath, InputStream is,
			FileOutputStream outputStream, CloseableHttpResponse respond) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException io) {
			logger.error("关闭异常：" + httpPath + "," + io.getMessage(), io);
		}
		
		try {
			if (respond != null) {
				respond.close();
			}
		} catch (Exception e) {
			logger.error("关闭异常：" + httpPath + "," + e.getMessage(), e);
		}
		
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("关闭异常：" + httpPath + "," + e.getMessage(), e);
		}
	}
	
	/**
	 * 描述:获取html的内容片段
	 * 
	 * @param httpPath
	 *            :请求路径
	 * @return
	 * @throws Exception
	 */
	public static String getHtmlContent(String httpPath,List<NameValuePair> urlParameters,String refer) throws Exception {
		return getHtmlContent( httpPath, urlParameters, refer,false);
	}
	
	/**
	 *改变UA 
	 */
	private static void changeUa(){
		UA = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537."+(int)(Math.random()*1000) ;
	}
	
	/**
	 * 描述:获取html的内容片段
	 * 
	 * @param httpPath
	 *            :请求路径
	 * @return
	 * @throws Exception
	 */
	public static String getHtmlContent(String httpPath,List<NameValuePair> urlParameters,String refer,boolean changeUA) throws Exception {
		
		// 一般爬虫请求都用Get，Get请求在HTTP请求协议里代表安全的查看:这个请求对象里可以添加http的请求头等
		
		URIBuilder uriBuilder = new URIBuilder(httpPath.trim());
		uriBuilder.setParameters(urlParameters);
		HttpGet httpGet= new HttpGet(uriBuilder.build());
		if(!StringUtils.isEmpty(refer)){
			httpGet.addHeader(new BasicHeader("referer", refer));
		}
		
		// 设置Get请求头的 User-Agent (模拟代理浏览器信息)
		if(changeUA){
			changeUa();
		}
		
		httpGet.setHeader( "User-Agent", HttpUtil.UA); //+new Random(1).nextInt(1000));
//		httpGet.setHeader( "User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537."); //+new Random(1).nextInt(1000));
//		httpGet.setHeader("Cookie", "x-wl-uid=1o36XJZ+NsTtfHjt3KyJovicuOcTgYm6lccMIzcZB898cUvrYz/4c7Dx31z2q1UplbuimZ5kbOzY=; session-id=257-6158185-0705766; session-id-time=2082787201l; ubid-acbde=258-1570618-6095466; session-token=/DPNfIxCHJjcz85Xar3wmHIMKTobQUpYk33SOjtbh51RHqGlfThSnspT88q1XyanGZiITG2TteAdYdWkMoczzENeEdTuYwbHYMugVlJds32xYuNBfd5TJO4Kw+oVhDPDpchrOuu4l5iLSTT40ffA9Y5+QvPXPeSfzZTyeZEklfw1EOWEPWctDC3/A7ApKMH+; i18n-prefs=EUR; csm-hit=tb:s-DJ8WFTQDT6T96VKY45JB|1562203001073&t:1562203002110&adb:adblk_no");
		// 用浏览器模拟对象httpClient，发送一个Get请求:可以通过这个响应对象获得很多http的响应信息
		InputStream is = null;
		FileOutputStream outputStream = null;
		CloseableHttpResponse respond = null;
		try {
			
			respond = httpclient.execute(httpGet);
			respond.setHeader("Connection", "close");
			if (HttpStatus.SC_OK != respond.getStatusLine().getStatusCode()) {
				httpGet.abort();
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
			// 获取返回的网页实体
			HttpEntity entity = respond.getEntity();
			if (entity != null) {
				is = entity.getContent();
				return IOUtils.toString(is);
			} else {
				logger.error("请求异常:" + httpPath + "," + respond.getStatusLine().getStatusCode());
				return null;
			}
		} catch (Exception e) {
			logger.error("请求异常：" + httpPath + "," + e.getMessage(), e);
		} finally {
			closeIO(httpPath, is, outputStream, respond);
		}
		return null;
	}
	
	
	public static  String getTaoBaoProductUrl(String id, String sign){
		JsonObject json = new JsonObject();  //586451114773
		json.addProperty ("id", id);
		json.addProperty("itemNumId", "596549543120");
		json.addProperty("exParams", String.format("{\"id\":\"%s\"}","596549543120"));
		return "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?appKey=12574478&sign="+sign+"&data="+URLEncoder.encode(json.toString());
	}
	
	public static  String getTaoBaoProductDescUrl(String id, String sign){
//		JsonObject json = new JsonObject();  //586451114773
//		json.addProperty("data", );
		
		String data = URLEncoder.encode(String.format("{\"id\":\"%s\"}",id)) ; //{"id":"596549543120","type":"1"};
		
		String url = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdesc/6.0/?appKey=12574478&sign="+sign+"&api=mtop.taobao.detail.getdesc&data="+data;
		return url;
	}
	
	

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
	 * @param args
     * @throws Exception 
	 */ 
	public static void main2(String[] args) throws Exception {
		
		String cookies ="ASP.NET_SessionId=g1a33fpjqij4xiv5nflo4ghh;FEIGUA=UserId=d1efe80e130f3a13&NickName=67572069fec55bcf0b20ab8a57de92bb&checksum=7913eff0da59&FEIGUALIMITID=e1d45de3c8c44868b4f1d5fa1dbe0fe1;FEIGUAUNIONID=45199ae6806a9d59b09afcfc2171cba600af2e588840c849b95d2d5ca00ba8d2;" ;
//		String cookies ="ASP.NET_SessionId=myvmg5jdg4yiuqn554vy0soq; Hm_lvt_b9de64757508c82eff06065c30f71250=1565081905,1565082868,1565320036,1565767251; Hm_lpvt_b9de64757508c82eff06065c30f71250=1565767251; FEIGUA=UserId=d1efe80e130f3a13&NickName=67572069fec55bcf0b20ab8a57de92bb&checksum=95370cc3e2b4&FEIGUALIMITID=1ae4858503504f2a82b04e26ee577c6b; FEIGUAUNIONID=45199ae6806a9d59b09afcfc2171cba600af2e588840c849b95d2d5ca00ba8d2; SaveUserName=; _uab_collina=156576725969112797677744";
		LoginAuth loginAuth = new LoginAuth();
		loginAuth.setCookie(cookies );
		loginAuth.setUa("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		HttpData httpData = new HttpData(loginAuth);
		httpData.setReferer( "https://dy.feigua.cn/Member" );
		String url = "https://dy.feigua.cn/Blogger/Detail?id=6520202&timestamp=1565878784&signature=ead5d5f0729017ab591e481fe4c1e6c3";
		String content = HttpUtil.getHtmlContent(url,httpData);
		content =removeSpecilChar(content);
		String s = FilterUtil.cutString(content,"地区：<span>" , "</span>");
    	 System.out.println( s  );
    	
		/*String url ="https://dy.feigua.cn/Login/GetQrCode?timestamp=1565081904&signature=ff1d6a605877d5c0a27cb95a96321481&rn=210.11557494368515";
		System.out.println(content);*/
//		System.out.println(content);
		/*SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time=dateformat1.format(date);
		System.out.println(time);*/
		
		//img13.360buyimg.com/jfs/t1/58021/5/4453/81985/5d22a9acEc0bca3d7/ffba2ec0c85927cb.jpg 
		/*
		for (int i = 0; i <1; i++) {
			
		
		
//		String url = "https://www.amazon.de/Firsthgus-Wandleuchte-Nachttischlampe-schlafzimmer-wandleuchte/dp/B07TG1GVJT/ref=sr_1_39?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&keywords=Wandleuchte&qid=1562147619&refinements=p_n_availability%3A225433031&rnid=225431031&s=lighting&sr=1-39";
		String url = "https://www.amazon.de/MOKIU-Luftk%C3%BChler-Klimaanlage-Klimager%C3%A4t-Leistungsstufen/dp/B07SQ4RFMJ/ref=sr_1_8?__mk_de_DE=%C3%85M%C3%85%C5%BD%C3%95%C3%91&keywords=Mini+Luftk%C3%BChler&qid=1562300279&refinements=p_n_availability%3A419126031&rnid=419124031&s=kitchen&sr=1-8";
		HashMap map = new HashMap<String, String>();
		map.put("keywords", HttpUtil.getQueryString(url, "keywords"));
		map.put("qid", HttpUtil.getQueryString(url, "qid"));
		map.put("rnid", HttpUtil.getQueryString(url, "rnid"));
		String s= HttpUtil.getHtmlContent(url,map,"",false);
		
		System.out.println(s.indexOf("Derzeit nicht verfügbar")==-1);
		
		String asin = s.substring(s.indexOf("ASIN</td><td"));
		  asin = asin.replace("ASIN</td><td","");
		asin = asin.substring(0, asin.indexOf("</tr>"));
		asin = asin.substring(asin.indexOf(">")+1,asin.indexOf("<"));
	 
		System.out.println(asin);
		
		
		//品牌可能为空
		String  marke="";
		try {
			if(s.indexOf("Technische Details")!=-1){
			 marke = s.substring(s.indexOf("Technische Details"));
			 marke = marke.substring(marke.indexOf("Marke"), marke.indexOf("</tr>"));
			 marke = marke.substring(marke.indexOf("value")+7,marke.lastIndexOf("</td>"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String rang = "";
		String paiming="";
		if(s.indexOf("Amazon Bestseller-Rang")!=-1){
			rang = s.substring(s.indexOf("Amazon Bestseller-Rang"));
			rang = rang.substring(0, rang.indexOf("</tr>"));
			paiming =rang.substring(rang.indexOf("Nr."),rang.indexOf("("));
			rang = rang.substring(rang.indexOf("class=\"zg_hrsr_rank\"") );
			paiming+=","+rang.substring(rang.indexOf("Nr."), rang.indexOf("</span>"));
			
		}
		
		String von = s.substring(s.indexOf("id=\"bylineInfo\""));
		von = von.substring(0, von.indexOf("</a>"));
		von = von.substring(von.lastIndexOf(">")+1);
		System.out.println("von"+von);
		System.out.println(paiming);
		
		System.out.println(s.indexOf("Amazon Bestseller-Rang")==-1);

		}
	
	
//		String ordergood = getQuestionCnt(cookies);
//		String ordergood = HttpUtil.getHtmlContent("http://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?isAbrdSellerYn=&isItalyAgencyYn=&listType=orderingConfirm&method=getUnapprovedOrderTotal", cookies);
//		String ordergood = getOrderGood( cookies);  //获取订单的数量
//		System.out.println(ordergood);
		//获取产品数量
//		getProductNum(cookies);
		
		//提取留言 ？？？？？
		
		
		/*
		System.out.println(s);
		String msg_count= JsonUtil.getJSONValue("msg_count",s);
		if("0".equals(msg_count)){
			System.out.println("没有数据啦！！！！！！！！！！！！！！！！！！");
		}
		String general_msg_list= JsonUtil.getJSONValue("general_msg_list",s);
		JSONObject jsonObject = JSONObject.parseObject(general_msg_list);
		System.out.println(jsonObject);
		List<String> list = JsonUtil.getJSONListValue("list->app_msg_ext_info->content_url", jsonObject.toJSONString());
		for (String contenturl : list) {
			System.out.println(contenturl);
		}
		System.out.println(general_msg_list);
		
		
	/*	String baseContent = Jsoup.clean(s, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
		String newText =  baseContent.replaceAll("\\s{2,}", "\n");
		String trueContent = newText.replaceFirst("\n", "").trim();
 System.out.println(trueContent);*/
//		String text = Jsoup.clean(s,Whitelist.none());
//		Document doc  = Jsoup.connect("https://mp.weixin.qq.com/s?__biz=MjM5ODYxMDA5OQ==&mid=2651962361&idx=1&sn=67bdcf5aa7435db757ace735f9968767&chksm=bd2d0e258a5a8733774350409ae2f3f1684e1536c105dbe92b1e9b3dbdaf83f2f67fbe7dad9b").execute().parse();
		
		
		
//	    Parser parser = Parser.htmlParser(); // createParser(new String(inputHtml.getBytes(),"GBK"), "GBK");
//	    parser.parse(html, baseUri)
				   
		/*Document doc =  Jsoup.parse(s);
		Element ele = doc.getElementById("js_article");
		
		WebClient  webClient = new WebClient();
		  HtmlPage page = webClient. .getPage(url);*/
//		String content = doc.body().select(cssQuery)
		
//		Element limit= Xsoup.select(doc, "//body/div[@id='js_article']");
//		Element limit = doc.body().selectFirst("div[class=html]");
		
/*		String s = HttpUtil.getHtmlContent("https://mp.weixin.qq.com/cgi-bin/appmsg?token=1194562356&lang=zh_CN&f=json&ajax=1&random=0.7707573761648039&action=list_ex&begin=5&count=10&query=&fakeid=MjM5ODYxMDA5OQ%3D%3D&type=9","UTF-8",cookies);
		List<String> list  = JsonUtil.getJSONListValue("app_msg_list->link", s);
		System.out.println(list.toString());
*/
		
///		String s = HttpUtil.postHtmlContent("https://item.taobao.com/item.htm?id=591758162649&wwlight=cntaobao%E5%9C%A8%E5%81%9A%E4%B8%80%E4%BC%9A-%7B591758162649%7D", map, cookies);
		/*
		 * String s = HttpUtil.getHtmlContent("https://detail.ju.taobao.com/home.htm?spm=608.2291429.102212b.1.6fd14f84Vw9Q1B&id=10000206429489&item_id=589814336859","gbk","");
		System.out.println(s);
//		String s = HttpUtil.getHtmlContent("https://detail.tmall.com/item.htm?spm=608.7065813.ne.1.52b72c7eW6AVos&id=548712023912&tracelog=jubuybigpic","GBK",  cookies);
		
		//天猫旗舰店
		if(s.indexOf("seller_nickname")!=-1){
			s = s.substring(s.indexOf("seller_nickname"));
			s = s.substring(0,s.indexOf("/>"));
			s = s.replace("seller_nickname\" value=\"", "");
			s = s.replace("\"", "");
		}else if(s.indexOf("sellerNick")!=-1){ //淘宝店铺
			s = s.substring(s.indexOf("sellerNick"));
			s = s.substring(0,s.indexOf(","));
			s = s.replace("sellerNick", "");
			s = s.replace("'", "");
			s = s.replace(":", "");
			s = s.replaceAll(" ", "");
		}else{
			s = s.substring(s.indexOf("data-nick="));
			s = s.substring(0,s.indexOf("data-tnick"));
			s = s.replace("data-nick=", "");
			s = s.replace("\"", "");
			s = s.replaceAll(" ", "");
		}*/
		
		
//		System.out.println("::"+s);  //获取淘宝的旺旺号，，，，商机。。。。
	/*	String cookies  = "Cookie: JSESSIONID=ABAAABAAADAAAEE801DB28141E5E5674DE0DC5DB4DCB663; user_trace_token=20190610101250-c276a2fd-a5c6-4c89-ad32-6927e8e8d726; _ga=GA1.2.1865789.1560132772; _gid=GA1.2.1002606687.1560132772; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1560132772; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1560132772; sajssdk_2015_cross_new_user=1; LGSID=20190610101251-402944a2-8b25-11e9-a22c-5254005c3644; PRE_UTM=; PRE_HOST=; PRE_SITE=; PRE_LAND=https%3A%2F%2Fpassport.lagou.com%2Flogin%2Flogin.html%3Fsignature%3DFE1B4782C7896ADA0AD04991D0AE154F%26service%3Dhttps%25253A%25252F%25252Feasy.lagou.com%25252Ftalent%25252Fsearch%25252Flist.htm%25253Fkeyword%25253D%252525E6%2525258B%2525259B%252525E8%25252581%25252598%252526pageNo%25253D1%252526city%25253D%252525E5%2525258C%25252597%252525E4%252525BA%252525AC%252526education%25253D%252525E6%2525259C%252525AC%252525E7%252525A7%25252591%252525E5%2525258F%2525258A%252525E4%252525BB%252525A5%252525E4%252525B8%2525258A%252526workYear%25253D3%252525E5%252525B9%252525B4-10%252525E5%252525B9%252525B4%252526industryField%25253D%252525E4%252525B8%2525258D%252525E9%25252599%25252590%252526expectSalary%25253D13k-25k%252526isBigCompany%25253D1%252526searchVersion%25253D1%26action%3Dlogin%26serviceId%3Daccount%26ts%3D1560132770821; LGRID=20190610101251-4029464a-8b25-11e9-a22c-5254005c3644; LGUID=20190610101251-402946b9-8b25-11e9-a22c-5254005c3644; LG_LOGIN_USER_ID=55ef98c339cb964f39a6aefcd5fc56c4f742716b0cef8ae9; LG_HAS_LOGIN=1; _putrc=CCDB5468444C87D5; login=true; unick=%E7%AE%A1%E7%90%86%E5%91%98; mds_login_authToken=\"KzgomPGgZr/cdGrbZTwQ9cSVFScgaMbQ2fIyfk2PdOcF+EJaG/PAaYAfgAczGLKhzBF12UMcv8fj3ZwfmqgOBLhHKPEY8Aag9cRFsuC/r9QUoMgkxbpN4ZC2TFC8UhILSSBb6+9lkIeG7k2TfxPa+ZloymJYA0mX6lndoHIFAE54rucJXOpldXhUiavxhcCELWDotJ+bmNVwmAvQCptcy5e7czUcjiQC32Lco44BMYXrQ+AIOfEccJKHpj0vJ+ngq/27aqj1hWq8tEPFFjdnxMSfKgAnjbIEAX3F9CIW8BSiMHYmPBt7FDDY0CCVFICHr2dp5gQVGvhfbqg7VzvNsw==\"; mds_u_n=%5Cu7ba1%5Cu7406%5Cu5458; mds_u_ci=165993; mds_u_cn=%5Cu5317%5Cu4eac%5Cu4fe1%5Cu94fe%5Cu79d1%5Cu6280%5Cu6709%5Cu9650%5Cu516c%5Cu53f8; mds_u_s_cn=%5Cu4fe1%5Cu94fe%5Cu79d1%5Cu6280; gate_login_token=4d4b0968d541ac3df831fa1ca75a4ed520d3dd36bce2e4ac; href=https%3A%2F%2Feasy.lagou.com%2Ftalent%2Fsearch%2Flist.htm%3Fkeyword%3D%25E6%258B%259B%25E8%2581%2598%26pageNo%3D1%26city%3D%25E5%258C%2597%25E4%25BA%25AC%26education%3D%25E6%259C%25AC%25E7%25A7%2591%25E5%258F%258A%25E4%25BB%25A5%25E4%25B8%258A%26workYear%3D3%25E5%25B9%25B4-10%25E5%25B9%25B4%26industryField%3D%25E4%25B8%258D%25E9%2599%2590%26expectSalary%3D13k-25k%26isBigCompany%3D1%26searchVersion%3D1; X_HTTP_TOKEN=493a485e1cc9d6b690823106512303ea9c4f3dc5aa; sensorsdata2015session=%7B%7D; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%227200826%22%2C%22%24device_id%22%3A%2216b3f28e00f3d-0cf207feec78f5-474a0521-1764000-16b3f28e010368%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_referrer_host%22%3A%22%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24os%22%3A%22Windows%22%2C%22%24browser%22%3A%22Chrome%22%2C%22%24browser_version%22%3A%2259.0.3071.115%22%2C%22easy_company_id%22%3A%22165993%22%2C%22lagou_company_id%22%3A%22174497%22%7D%2C%22first_id%22%3A%2216b3f28e00f3d-0cf207feec78f5-474a0521-1764000-16b3f28e010368%22%7D; qimo_seosource_551129f0-7fc2-11e6-bcdb-855ca3cec030=%E5%85%B6%E4%BB%96%E7%BD%91%E7%AB%99; qimo_seokeywords_551129f0-7fc2-11e6-bcdb-855ca3cec030=%E6%9C%AA%E7%9F%A5; accessId=551129f0-7fc2-11e6-bcdb-855ca3cec030; pageViewNum=2; gray=resume";
		String positionId = "6872761";
		String userId = "8179270";
		HashMap<String,String> map  = new HashMap<>();
		map.put("city", "北京");
		map.put("education", "本科及以上");
		map.put("expectSalary", "13k-25k");
		map.put("industryField", "不限");
		map.put("isBigCompany", "1");
		map.put("keyword", "招聘");
		map.put("pageNo", "1");
		map.put("searchVersion", "1");
		map.put("workYear", "3年-10年");
		String s = HttpUtil.postHtmlContent("https://easy.lagou.com/talent/search/list.json", map, cookies);
		System.out.println(s);*/
//		sendContent(cookies, positionId, userId);
		
//		System.out.println(URLDecoder.decode("https://easy.lagou.com/talent/search/list.htm?keyword=招聘&pageNo=4&city=北京&education=本科及以上&workYear=3年-10年&industryField=不限&expectSalary=13k-25k&isBigCompany=1&searchVersion=1", "UTF-8")  );
		
//		getPositionId();
		
		 
	 /*	String url =getTaoBaoProductDescUrl("596384214195", "aasdf"); //4066665511
//		String content = HttpUtil.getHtmlContent("https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdesc/6.0/?jsv=2.5.1&appKey=12574478&t=1560041755324&sign=1097fbb45b1759be09894edd79e3fd14&api=mtop.taobao.detail.getdesc&v=6.0&isSec=0&ecode=0&AntiFlood=true&AntiCreep=true&H5Request=true&type=jsonp&dataType=jsonp&callback=mtopjsonp2&data=%7B%22spm%22%3A%22a1z10.5-c-s.w4002-21086417433.37.22fb5e45EIqmcr%22%2C%22id%22%3A%22596549543120%22%2C%22type%22%3A%221%22%7D");
		String content = HttpUtil.getHtmlContent(url);
		JSONObject jsonObject = JSONObject.parseObject(content);
		JSONObject data = (JSONObject) jsonObject.get("data");
		String pcDescContent = ""+ data.get("pcDescContent");
		Document doc = Jsoup.parse(pcDescContent);
		Elements eles = Xsoup.select(doc, "//img").getElements();
		for (Element element : eles) {
			String src = "https:"+element.attr("src");
		}
		
		System.out.println(url);
		System.out.println(content); */
		 
		
		
		/*
		JSONArray jsonArray = null;
		JSONObject jsonObject=null;
		if(content.startsWith("[")){
			jsonArray = JSONArray.parseArray(content);
		}else{
			jsonObject=JSONObject.parseObject(content);
		}*/
		
		
		
		/*System.out.println(content);
		JSONObject jsonObject = JSONObject.parseObject(content);
		String succ = ""+jsonObject.get("ret");
		if(succ.indexOf("SUCCESS")==-1){
			System.out.println("请求失败");
			return ;
		}
		JSONObject data = (JSONObject) jsonObject.get("data");
		JSONObject skuBase = (JSONObject) data.get("skuBase");
		JSONArray jsonArray = (JSONArray) skuBase.get("props");
		JSONObject size = (JSONObject) jsonArray.get(0);
		JSONArray sizeArray  = (JSONArray) size.get("values");
		for (Object object : sizeArray) {
			System.out.println(((JSONObject)object).get("name"));
		}
		
		JSONObject color = (JSONObject) jsonArray.get(1);
		JSONArray colorArray  = (JSONArray)  color.get("values");
		for (Object object : colorArray) {
			System.out.println(((JSONObject)object).get("name"));
		}*/
		
//		String s= URLEncoder.encode("{\"originid\":\"\"}", "UTF-8");  
		
//		String url = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?jsv=2.5.1&appKey=12574478&t=1558888333992&sign=34cc81b35a28424a250b931972cd707d&api=mtop.taobao.detail.getdetail&v=6.0&isSec=0&ecode=0&AntiFlood=true&AntiCreep=true&H5Request=true&ttid=2018%40taobao_h5_9.9.9&type=jsonp&dataType=jsonp&callback=mtopjsonp1&data=%7B%22spm%22%3A%22a1z10.1-c.w4004-12534775751.7.4b257805iRnjyc%22%2C%22id%22%3A%22586451114773%22%2C%22itemNumId%22%3A%22586451114773%22%2C%22exParams%22%3A%22%7B%5C%22spm%5C%22%3A%5C%22a1z10.1-c.w4004-12534775751.7.4b257805iRnjyc%5C%22%2C%5C%22id%5C%22%3A%5C%22586451114773%5C%22%7D%22%2C%22detail_v%22%3A%228.0.0%22%2C%22utdid%22%3A%221%22%7D";
		
	/*	JsonObject json = new JsonObject();  //586451114773
		for (long i = 114773; i <= 114773; i++) {
			
			json.addProperty ("id", "586451"+i);
			json.addProperty("itemNumId", "586451"+i);
			json.addProperty("exParams", String.format("{\"id\":\"%s\"}","586451"+i));
			
			String url = "https://h5api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?data="+URLEncoder.encode(json.toString());
			String content = HttpUtil.getHtmlContent(url,"utf-8");  //材质保证
			System.out.println(content);
		}*/
		/*JSONObject jsonObject = (JSONObject) JSONObject.parseObject(content).get("item");
		JSONArray array = (JSONArray) jsonObject.get("images");
				for (int i = 0; i < array.size(); i++) {
					System.out.println("https://cf.shopee.ph/file/"+array.get(i));
				}
		*/
//		content = HttpUtil.getHtmlContent("http://item.jd.com/"+p1+".html");
		
	}
/*
	private static String getTwoImage() throws Exception {
		String s =	HttpUtil.getHtmlContent("","GBK","");
		s  = s.substring(s.indexOf("id=\"spec-list\""));
		s = s.substring(s.indexOf("<li"),s.indexOf(" </div>"));
		String[] lis = s.split("<li");
		String li = lis.length >2 ? lis[2] : lis[1];
//		StringUtils.delHtml(s);
		String dataurl =  li.substring(li.indexOf("src='")+4, li.indexOf("data-url='"));
		dataurl = dataurl.replace("img.com/n5", "img.com/n1");
		dataurl = dataurl.replace("'", "");
		return dataurl;
	}

	private static String getQuestionCnt(String cookies) throws Exception {
		String ordergood = HttpUtil.getHtmlContent("http://soffice.11st.co.kr/marketing/SellerMenuAction.tmall?method=getEmerMainAlertStatAjax", cookies);
		ordergood = ordergood.replace("(", "");
		ordergood = ordergood.replace(")", "");
		System.out.println(JsonUtil.getJSONValue("emerStat00", ordergood));
		return ordergood;
	}
	
	 * 获取商品的数量
	 
	private static void getProductNum(String cookies) throws Exception {
		String s =  HttpUtil.getHtmlContent("https://soffice.11st.co.kr/product/SellProductAction.tmall?method=getSellProductList","euc-kr",cookies);
		s = s.substring(s.indexOf("판매중 : "));
		s = s.substring(s.indexOf(">")+1,s.indexOf("</em>"));
		System.out.println(s);
	}*/

	private static String getHotVideoUrl(String keyword, Integer day,
			String sort, Integer page) {
		String url ="https://dy.feigua.cn/Aweme/Search?keyword="+keyword+"&tag=&likes=0&hours="+day*24+"&duration=0&gender=0&age=0&province=0&city=0&sort="+sort+"&ispromotions=0&page="+page;
		return url;
	}
	
	/**
	 * 获取订单数量
	 * @param url
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public static String getOrderGood( String cookies)
			throws Exception {
		String url = "http://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?isAbrdSellerYn=&isItalyAgencyYn=&listType=orderingConfirm&method=getUnapprovedOrderTotal";
//		String url = "https://soffice.11st.co.kr/escrow/UnapprovedOrder.tmall?method=getUnapprovedOrderTotal";
		HashMap map = new HashMap<>();
		map.put("listType", "orderingConfirm");
		map.put("isAbrdSellerYn", "");
		map.put("isItalyAgencyYn", "");
		String s =  HttpUtil.postHtmlContent(url,map,cookies);
		System.out.println(s);
		String ordergood= JsonUtil.getJSONValue("unapprovedOrderTotal->order_good_202", s);
		return ordergood;
	}
	
	
	private static void sendContent(String cookies, String positionId,
			String userId) throws Exception {
		HashMap map = new HashMap<>();
		map.put("cUserIds", userId);
		String content = HttpUtil.postHtmlContent("https://easy.lagou.com/im/session/greetingList.json?", map, cookies);
		JSONObject res = JSONObject.parseObject(content);
		JSONObject con = (JSONObject) res.get("content");
		JSONObject data = (JSONObject) con.get("data");
		JSONArray greetingList = (JSONArray) data.get("greetingList");
		String sendContent = "";
		 /*
		for (Object object : greetingList) {
			JSONObject json  = (JSONObject) object ; 
			if("true".equals(""+json.get("defaults"))){
				sendContent = ""+json.get("content");
				break;
			}
		}*/
		
		System.out.println(sendContent);
		
		HashMap param = new HashMap<>();
		param.put("inviteDeliver", "true");
		param.put("positionId", positionId);
		param.put("greetingContent", sendContent); //sendContent
        
		content = HttpUtil.postHtmlContent("https://easy.lagou.com/im/session/batchCreate/"+userId+".json?", param, cookies);
		System.out.println(content);
	}

	public static String urlEncode(String str) throws UnsupportedEncodingException {
	    StringBuilder sb = new StringBuilder();
	    //获得UTF-8编码的字节数组
	    byte[] utf8 = str.getBytes("UTF-8");
	    for (byte b : utf8) {
	      System.out.println(b);
	      //将字节转换成16进制，并截取最后两位
	      String hexStr = Integer.toHexString(b);
	      String temp = hexStr.substring(hexStr.length() - 2);
	      //添加%
	      sb.append("%");
	      sb.append(temp);
	    }
	    return sb.toString();
	  }
	 

/*
	private static void getPageData(String key,Integer page ) throws Exception {
		String url = "https://search.jd.com/s_new.php";
		String refer = "https://search.jd.com/Search";
		Integer s=( page-1)*30+1;
		
		List<NameValuePair> urlParameters  = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("keyword", key)); //
		urlParameters.add(new BasicNameValuePair("enc", "utf-8"));
		urlParameters.add(new BasicNameValuePair("qrst", "1"));
		urlParameters.add(new BasicNameValuePair("rt", "1"));
		urlParameters.add(new BasicNameValuePair("stop", "1"));
		urlParameters.add(new BasicNameValuePair("vt", "2"));
//		urlParameters.add(new BasicNameValuePair("wq", key)); //"机械手表"
		urlParameters.add(new BasicNameValuePair("psort", "4"));
		urlParameters.add(new BasicNameValuePair("cod", "1"));
		urlParameters.add(new BasicNameValuePair("page", page+""));
		urlParameters.add(new BasicNameValuePair("s", s+""));
		
		
		urlParameters.add(new BasicNameValuePair("cid2", "5026"));
		urlParameters.add(new BasicNameValuePair("cid3", "13673"));
		
		if(page%2==0){
			urlParameters.add(new BasicNameValuePair("scrolling", "y"));
		}
		
		
		String content = HttpUtil.getHtmlContent(url,urlParameters,refer);
		if(content.indexOf("<script>")!=-1){
			content= content.substring(0,content.indexOf("<script>"));
		}
		
//		System.out.println(content);
		
		Document doc =null;
		Document element = null;
		List<Node> nodes =null;
		doc = Jsoup.parse(content); 
		
		if(page%2!=0){
			content = Xsoup.select(doc, "//body/div/ul").get();
			content = content.substring(content.indexOf("li")-1);
			content = content.substring(0,content.lastIndexOf("ul")-2);
			doc = Jsoup.parse(content);
		}
		
		nodes = doc.body().childNodes();
		
		
		String href = "";
		String shopName = "";
		for (Node node : nodes) {
			if(StringUtils.isEmpty(node.toString())){
				continue;
			}
			
			element =  Jsoup.parse(node.toString());
//			System.out.println(node.toString());
			content = Xsoup.select(element, "//body/li/div/div[contains(@class,'p-shop')]/span/a").get();
			href ="http:"+content.substring(content.indexOf("href")+6, content.indexOf("html")+4);
			shopName =content.substring(content.indexOf("title")+6, content.indexOf("</a>"));
			shopName=shopName.substring(shopName.indexOf(">"));
			JdEntity e = new JdEntity();
			e.setShopName(shopName);
			e.setUrl(url);
			System.out.println(href+"|"+shopName);
		}
	} */
	
	private static Map<String, String> toMap(String url) {
		  int index = url.indexOf("?");
	        String param = url.substring(index+1);

	        String[] params = param.split("&");

	        Map<String,String> map = new HashMap<>();

	        for (String item:params) {
	            String[] kv = item.split("=");
	            map.put(kv[0],kv.length==2 ? kv[1]:"" );
	        }
	        return map;
	        
	}

	public static String getQueryString(String url, String name) {
		String  s= toMap(url).get(name) ;
		if(s!=null){
			return s.trim();
		}
		return s;
	}
	 
}
