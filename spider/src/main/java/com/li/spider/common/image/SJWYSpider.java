package com.li.spider.common.image;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/7/29 14:43
 */
public class SJWYSpider {
    public static void main(String[] args) {
        String uri = "http://desk.zol.com.cn/bizhi/9318_113764_2.html";
        doDownload(uri);
    }

    private static void doDownload(String uri) {
        try {
            // 解析主页
            Connection connect = Jsoup.connect(uri);
            connect.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            Document document = connect.get();
//            System.out.println(document);

            Element showImg = document.getElementById("showImg");
            Elements lis = showImg.getElementsByTag("li");
            for (Element li : lis) {
                String attr = li.getElementsByTag("a").first().getElementsByTag("img").first().attr("src");
                if (StringUtils.isEmpty(attr)) {
                    attr = li.getElementsByTag("a").first().getElementsByTag("img").first().attr("srcs");
                }
                String replace = attr.replace("144x90", "4096x2160");
                System.out.println(replace);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
