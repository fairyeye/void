package com.li.spider.common.image;

import cn.hutool.http.HttpUtil;
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
public class ZOLSpiderByRank {
    public static void main(String[] args) {
        String uri = "http://desk.zol.com.cn";
        parseIndex(uri);
    }

    private static void parseIndex(String uri) {
        try {
            // 解析主页
            Connection connect = Jsoup.connect(uri);
            connect.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            Document document = connect.get();

            Elements rankLists = document.getElementsByClass("rank-list");
            for (Element rankList : rankLists) {
                Elements lis = rankList.getElementsByTag("li");
                for (Element li : lis) {
                    String href = li.getElementsByTag("a").first().attr("href");
                    doDownload(uri + href);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doDownload(String href) {
        try {
            // 解析主页
            Connection connect = Jsoup.connect(href);
            connect.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            Document document = connect.get();
//            System.out.println(document);

            // 获取当前图片支持的最大分辨率
            String targetResolution = document.getElementById("tagfbl").getElementsByTag("a").first().attr("id");
            String titleName = document.getElementById("titleName").text();

            String pathName = "E:/file/image/" + titleName + "/";

            Element showImg = document.getElementById("showImg");
            Elements lis = showImg.getElementsByTag("li");
            for (Element li : lis) {
                String fileName  = li.getElementsByTag("em").first().text();
                String attr = li.getElementsByTag("a").first().getElementsByTag("img").first().attr("src");
                if (StringUtils.isEmpty(attr)) {
                    attr = li.getElementsByTag("a").first().getElementsByTag("img").first().attr("srcs");
                }
                String replace = attr.replace("144x90", targetResolution);
                long size = HttpUtil.downloadFile(replace, pathName + fileName + ".jpg");
                System.out.println(replace + "下载完成。====>SIZE = " + size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
