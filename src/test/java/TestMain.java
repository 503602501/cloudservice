import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.feigua.entity.HttpData;
import com.mybatisplus.dao.domain.LoginAuth;
import com.us.codecraft.XElements;
import com.us.codecraft.Xsoup;
import com.util.FilterUtil;
import com.util.StringUtils;

public class TestMain {
	
	private static final String fileName = "D:\\workspace\\cloudservice\\file.log";
	
	public static void main(String[] args) throws Exception {
		
		String cookies ="ASP.NET_SessionId=g1a33fpjqij4xiv5nflo4ghh;FEIGUA=UserId=d1efe80e130f3a13&NickName=67572069fec55bcf0b20ab8a57de92bb&checksum=7913eff0da59&FEIGUALIMITID=e1d45de3c8c44868b4f1d5fa1dbe0fe1;FEIGUAUNIONID=45199ae6806a9d59b09afcfc2171cba600af2e588840c849b95d2d5ca00ba8d2;" ;
//		String cookies ="ASP.NET_SessionId=myvmg5jdg4yiuqn554vy0soq; Hm_lvt_b9de64757508c82eff06065c30f71250=1565081905,1565082868,1565320036,1565767251; Hm_lpvt_b9de64757508c82eff06065c30f71250=1565767251; FEIGUA=UserId=d1efe80e130f3a13&NickName=67572069fec55bcf0b20ab8a57de92bb&checksum=95370cc3e2b4&FEIGUALIMITID=1ae4858503504f2a82b04e26ee577c6b; FEIGUAUNIONID=45199ae6806a9d59b09afcfc2171cba600af2e588840c849b95d2d5ca00ba8d2; SaveUserName=; _uab_collina=156576725969112797677744";
		LoginAuth loginAuth = new LoginAuth();
		loginAuth.setCookie(cookies );
		loginAuth.setUa("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		HttpData httpData = new HttpData(loginAuth);
		httpData.setReferer( "https://dy.feigua.cn/Member" );
		String url = "https://dy.feigua.cn/Blogger/Detail?id=6306008&timestamp=1565767994&signature=bb47dbbb29e91770a14e9d52684c6657";
		String content =  getFansText("D:\\workspace\\cloudservice\\fans.log") ; //HttpUtil.getHtmlContent(url,httpData);
    	
		content= removeSpecilChar(content);
		String s1 = FilterUtil.cutString(content,"var bloggerId =" , ";");
		String bloggerId =s1.trim().replace("'", "");
		System.out.println("content:"+bloggerId);
		
		bloggerDetail(content);
		
		 
		content =  getFansText("D:\\workspace\\cloudservice\\location.log") ;
		content= removeSpecilChar(content);
		System.out.println("*************************");
		String  s = FilterUtil.cutString(content,"男" , "</div>");
		System.out.println(StringUtils.delHtml(s) );
		s = FilterUtil.cutString(content,"女" , "</div>");
		System.out.println(StringUtils.delHtml(s) );
		
		//
		Document doc = Jsoup.parse(content)  ;
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[1]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[1]/td[2]");
		System.out.println(s);
		
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[2]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[2]/td[2]");
		System.out.println(s);
		
		
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[3]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[3]/td[2]");
		System.out.println(s);
		
		
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[4]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[4]/td[2]");
		System.out.println(s);
		
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[5]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[5]/td[2]");
		System.out.println(s);
		
		System.out.println("****************城市如下*************");
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[1]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[1]/td[2]");
		System.out.println(s);
		
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[2]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[2]/td[2]");
		System.out.println(s);
		
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[3]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[3]/td[2]");
		System.out.println(s);
		
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[4]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[4]/td[2]");
		System.out.println(s);
		
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[5]/td[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/table[2]/tbody/tr[5]/td[2]");
		System.out.println(s);
		
		
		System.out.println("***************星座分布*****************");
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[1]/div[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[1]/div[3]");
		System.out.println(s);
		
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[2]/div[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[2]/div[3]");
		System.out.println(s);
		
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[3]/div[1]");
		System.out.println(s);
		s =  Xsoup.selectText(doc, "//div[@class='section-content']/ul/li[3]/div[3]");
		System.out.println(s);
		
    	 
 //        System.out.println("addressBook.txt内容为==> " + s);
	}

	private static void bloggerDetail(String content) {
		StringBuffer sb =  new StringBuffer(content); //getFileText();
   	    XElements elements =  Xsoup.select(sb.toString(), "//div[@class='nickname v-tag']/text()");
    	String nickname = elements.get();
    	String s = FilterUtil.cutString(sb.toString(),"抖音号：<span>" , "</span>");
    	
    	  s = FilterUtil.cutString(sb.toString(),"性别： <span>" , "</span>");
    	  
    	  s = FilterUtil.cutString(sb.toString(),"地区：<span>" , "</span>");
    	  
    	  s = FilterUtil.cutString(sb.toString(),"年龄：<span>" , "</span>");

    	  s = FilterUtil.cutString(sb.toString(),"简介：<span>" , "</span>");
    	  
    	  s = FilterUtil.cutString(sb.toString(),"简介：<span>" , "</span>");

    	  
    	  elements =  Xsoup.select(sb.toString(), "//div[@class='rank-show']/a/text()");
    	  
    	System.out.println(elements.get());  //排行榜
    	
    	System.out.println(content);
    	s = FilterUtil.cutString(content,">粉丝数" , "</span>");
//    	String fans = sb.toString().substring(sb.toString().indexOf("col-sm-4 fans js-fans-tip") ) ;
//    	fans = fans.substring(fans.indexOf("<span>"),fans.indexOf("</span>"));
//    	System.out.println(fans);
//    	s = s.substring(s.lastIndexOf(">")+1);
    	System.out.println(s);
    	System.out.println("粉丝数:"+StringUtils.delHtml(s) );  //粉丝数量
    	
    	  
    	elements =  Xsoup.select(sb.toString(), "//div[@class='col-sm-4 videos-count']/span/text()");
    	System.out.println(elements.get());
    	
//    	System.out.println(content);

    	//平均点赞
    	s = FilterUtil.cutString(content,"平均点赞" , "</span>");
    	System.out.println(StringUtils.delHtml(s) );
    	
    	
    	//集均评论
    	s = FilterUtil.cutString(sb.toString(),"集均评论" , "</span>");
    	System.out.println(StringUtils.delHtml(s));
    	
    	s = FilterUtil.cutString(sb.toString(),"集均分享" , "</span>");
    	
    	System.out.println("*************");
    	//最新7天
    	s = FilterUtil.cutString(sb.toString(),"最新作品数" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );
    	
    	s = FilterUtil.cutString(sb.toString(),"粉丝增量" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );
    	
    	s = FilterUtil.cutString(sb.toString(),"新增点赞" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );

    	s = FilterUtil.cutString(sb.toString(),"新增评论" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );
    	
    	s = FilterUtil.cutString(sb.toString(),"新增转发" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );
    	
    	//最新30天
    	System.out.println("********************");
    	String thirty = sb.toString().split("data-preview js-blogger-overview")[2];
    	s = FilterUtil.cutString(thirty,"最新作品数" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );
    	
    	s = FilterUtil.cutString(thirty,"粉丝增量" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );
    	
    	s = FilterUtil.cutString(thirty,"新增点赞" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );

    	s = FilterUtil.cutString(thirty,"新增评论" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );
    	
    	s = FilterUtil.cutString(thirty,"新增转发" , "</div>");
    	System.out.println(StringUtils.delHtml(s) );
    	
    	
    	
    	
	}

	public static StringBuffer getFileText() {
		//读取文件
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\workspace\\cloudservice\\file.log"), "UTF-8")); //这里可以控制编码
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return sb;
	}
	
	
	public static String getFansText(String filepath) {
		//读取文件
		BufferedReader br = null;
		StringBuffer sb = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8")); //这里可以控制编码
			sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
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
	 
	

}
