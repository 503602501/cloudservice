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

public class VideoTestMain {
	
	private static final String fileName = "D:\\workspace\\cloudservice\\file.log";
	
	public static void main(String[] args) throws Exception {
		
	/*	String cookies ="ASP.NET_SessionId=g1a33fpjqij4xiv5nflo4ghh;FEIGUA=UserId=d1efe80e130f3a13&NickName=67572069fec55bcf0b20ab8a57de92bb&checksum=7913eff0da59&FEIGUALIMITID=e1d45de3c8c44868b4f1d5fa1dbe0fe1;FEIGUAUNIONID=45199ae6806a9d59b09afcfc2171cba600af2e588840c849b95d2d5ca00ba8d2;" ;
//		String cookies ="ASP.NET_SessionId=myvmg5jdg4yiuqn554vy0soq; Hm_lvt_b9de64757508c82eff06065c30f71250=1565081905,1565082868,1565320036,1565767251; Hm_lpvt_b9de64757508c82eff06065c30f71250=1565767251; FEIGUA=UserId=d1efe80e130f3a13&NickName=67572069fec55bcf0b20ab8a57de92bb&checksum=95370cc3e2b4&FEIGUALIMITID=1ae4858503504f2a82b04e26ee577c6b; FEIGUAUNIONID=45199ae6806a9d59b09afcfc2171cba600af2e588840c849b95d2d5ca00ba8d2; SaveUserName=; _uab_collina=156576725969112797677744";
		LoginAuth loginAuth = new LoginAuth();
		loginAuth.setCookie(cookies );
		loginAuth.setUa("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
		HttpData httpData = new HttpData(loginAuth);
		httpData.setReferer( "https://dy.feigua.cn/Member" );
		String url = "https://dy.feigua.cn/Blogger/Detail?id=6306008&timestamp=1565767994&signature=bb47dbbb29e91770a14e9d52684c6657";*/
		String content =  getFansText("D:\\workspace\\cloudservice\\fans.log") ; //HttpUtil.getHtmlContent(url,httpData);
		Document doc =null;
		XElements elements =  Xsoup.select("<table>"+content+"<table>", "//tr");
		System.out.println(elements.list().size());
		for (String ss : elements.list()) {
			doc = Jsoup.parse("<table>"+ss+"</table>");
			String s = Xsoup.selectText(doc, "//div[@class='item-inner']/div[@class='item-title']/a");
			  s = Xsoup.selectText(doc, "//div[@class='item-times']");
			  s = Xsoup.select(doc, "//a[@class='source-play']/@href").get();
			  s = Xsoup.selectText(doc, "//div[@class='item-tag clearfix']/ul");
			  s = Xsoup.selectText(doc, "//i[@class='v-icon-set like']");
			  ss= removeSpecilChar(ss);
			  s = FilterUtil.cutString(ss,"v-icon-set like" , "<i");
			  s = s.replace("></i>", "");
			  s = s.replace("\"", "");
//			  System.out.println(s);
			  s = FilterUtil.cutString(ss,"v-icon-set comments" , "<i");
			  s = s.replace("></i>", "");
			  s = s.replace("\"", "");
//			  System.out.println(s);
			  s = FilterUtil.cutString(ss,"v-icon-set reply" , "<i");
			  s = s.replace("></i>", "");
			  s = s.replace("\"", "");
			  System.out.println(s);
		}
//		Document doc = Jsoup.parse()  ;
//		String s =  Xsoup.selectText(doc, "//table[@class='location-table']/tbody/tr[1]/td[1]");
    	
    	
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
