package org.zhengyj.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 *
 * @Author zhengyingjun
 * @Description ：demo启动类
 * 说明：例子来源于webmagic官方
 * @Date 2019/4/16
 **/


/**需要实现pageprocessor接口
 *
 */
public class Start implements PageProcessor {

    public static void main(String[] args) {
        Spider.create(new Start()).addUrl("http://blog.sina.com.cn/s/articlelist_1487828712_0_1.html")
                .run();
    }
    /**
     * 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
     */
    private Site site = Site.me()
            .setDomain("blog.sina.com.cn")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    /**
     * d 为页数
     */
    public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";
    /**
     * 文章页符合    “http://blog.sina.com.cn/s/blog_58ae76e80100g8au.html”，  blog_后属于页面信息在变化
     */
    public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";

    /**
     * 判断条件等
     * @param page
     */
    @Override
    public void process(Page page) {
        //列表页
        if (page.getUrl().regex(URL_LIST).match()) {
            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"articleList\"]").links().regex(URL_POST).all());
            //符合正则表达式的链接地址
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            //文章页
        } else {
            //标题
            page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2"));
            //内容
            page.putField("content", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']"));
            //日期
            page.putField("date",
                    page.getHtml().xpath("//div[@id='articlebody']//span[@class='time SG_txtc']").regex("\\((.*)\\)"));
        }
    }
    @Override
    public Site getSite() {
        return site;
    }
}
