package org.xavier.star.util;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class WeChatCrawlerUtil {
    //获取文章封面图片
    public static String getCoverUrl(String informationUrl) throws IOException {
        String picUrl = null;
        int flag;
        Document doc = Jsoup.connect(informationUrl).timeout(3000).get();
        String htmlString=doc.toString();
        flag=htmlString.indexOf("msg_cdn_url");
        while(htmlString.charAt(flag)!='\"'){
            flag++;
        }
        int beginIndex=++flag;
        while(htmlString.charAt(flag)!='\"')
            flag++;
        int endIndex=--flag;
        picUrl=htmlString.substring(beginIndex,endIndex);
        return picUrl;
    }
    //获取公众号名称
    public static String getName(String informationUrl) throws IOException {
        Document doc = Jsoup.connect(informationUrl).timeout(3000).get();
        Element names = doc.getElementById("js_name");
        return names.text();
    }
    //获取文章时间
    public static String getTime(String informationUrl) throws IOException {
        String time=null;
        Document doc = Jsoup.connect(informationUrl).timeout(3000).get();
        Elements scripts = doc.select("script");
        for (Element script : scripts) {
            String html = script.html();
            if (html.contains("document.getElementById(\"publish_time\")")) {
                int fromIndex = html.indexOf("s=\"");
                time=html.substring(fromIndex+3,fromIndex+13);
                return time;
            }
        }
        return time;
    }
    //获取文章标题
    public static String getTitle(String informationUrl) throws IOException {
        Document doc = Jsoup.connect(informationUrl).timeout(3000).get();
        Elements titles = doc.getElementsByClass("rich_media_title");
        return titles.text();
    }
    //获取公众号
    public static String getOfficialAccount(String informationUrl) throws IOException {
        Document doc = Jsoup.connect(informationUrl).timeout(3000).get();
        Elements metaValues = doc.getElementsByClass("profile_meta_value");
        return metaValues.get(0).text();
    }
    //获取公众号文章内容
    public static String getContent(String informationUrl) throws IOException {
        Document doc = Jsoup.connect(informationUrl).timeout(3000).get();
        Element metaValues = doc.getElementById("js_content");
        //String content = metaValues.html();//此行获取HTML
        assert metaValues != null;
        return metaValues.text();
    }
    //获取公众号真实链接
    public static String getTrueUrl(String informationUrl) throws IOException {
        Document doc = Jsoup.connect(informationUrl).timeout(3000).get();
        return doc.select("meta[property=og:url]").get(0).attr("content");
    }
    //获取公众号作者
    public static String getAuthor(String informationUrl) throws IOException {
        Document doc = Jsoup.connect(informationUrl).timeout(3000).get();
        return doc.select("meta[property=og:article:author]").get(0).attr("content");
    }

    //获取封面
    public static String getCover(String informationUrl) throws IOException {
        Document doc = Jsoup.connect(informationUrl).timeout(3000).get();
        return doc.select("meta[property=og:image]").get(0).attr("content");
    }

    public static void main(String[] args) throws IOException {
//        String url="https://mp.weixin.qq.com/s?__biz=MzkzMzI4MjMyNA==&mid=2247511296&idx=1&sn=ee7627523876bc4a1078f3d7e4ad68aa&source=41#wechat_redirect";
//        String url = "https://mp.weixin.qq.com/s/PgAjgWfrT0GL422laX4-2w";
        String url = "https://mp.weixin.qq.com/s/PgAjgWfrT0GL422laX4";
//        System.out.println(getTime(url));
        System.out.println("标题：" +  getTitle(url));
        System.out.println("公众号:" + getOfficialAccount(url));
        //System.out.println(getContent(url));
        System.out.println("作者：" + getAuthor(url));
        System.out.println("封面：" + getCoverUrl(url));
        System.out.println("真实链接：" + getTrueUrl(url));
    }
}

