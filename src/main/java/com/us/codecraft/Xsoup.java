package com.us.codecraft;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.us.codecraft.xsoup.w3c.NodeAdaptors;
import com.us.codecraft.xsoup.xevaluator.XPathParser;
import com.util.StringUtils;

/**
 * @author code4crafter@gmail.com
 */
public class Xsoup {

    /*-------------     XEvaluator         --------------- */

    public static XElements select(Element element, String xpathStr) {
        return XPathParser.parse(xpathStr).evaluate(element);
    }
    
    /**
     * 删除标签获取内容
     * @param element
     * @param xpathStr
     * @return
     */
    public static String selectText(Element element, String xpathStr) {
    	XElements xElements = select(  element,   xpathStr);
    	if(xElements==null || xElements.get()==null){
    		return "";
    	}
    	return StringUtils.delHtml(xElements.get() );
    }
    
    public static XElements select(String html, String xpathStr) {
        return XPathParser.parse(xpathStr).evaluate(Jsoup.parse(html));
    }
    
    public static XElements selectTable(String html, String xpathStr) {
    	if(html.indexOf("<table")!=-1){
    		html="<table>"+html+"</table>";
    	}
    	return XPathParser.parse(xpathStr).evaluate(Jsoup.parse(html));
    }
    
    public static XPathEvaluator compile(String xpathStr) {
        return XPathParser.parse(xpathStr);
    }

    /*-------------     W3cAdaptor         --------------- */

    public static org.w3c.dom.Element convertElement(Element element) {
        return NodeAdaptors.getElement(element);
    }

    public static org.w3c.dom.Document convertDocument(Document document) {
        return NodeAdaptors.getDocument(document);
    }

}
