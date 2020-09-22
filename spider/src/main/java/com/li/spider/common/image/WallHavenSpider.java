package com.li.spider.common.image;

import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/7/29 14:43
 * @descripttion 获取链接  配合Aria2下载图片
 */
public class WallHavenSpider {
    public static void main(String[] args) {
        String uri = "https://wallhaven.cc/toplist?page=";
        for (int i = 1; i < 150; i++) {
            parseIndex(uri + i);
        }
//        testConnect("https://w.wallhaven.cc/full/2e/wallhaven-2em38y.jpg");
    }

    private static void parseIndex(String uri) {
        try {
            // 解析主页
            Connection connect = Jsoup.connect(uri);
            connect.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            Document document = connect.get();

            // 获取图片链接
            String pathName = "E:/file/image/Toplist/img-links.txt";
            BufferedWriter output = new BufferedWriter(new FileWriter(pathName, true));

            // 获取当前页图片所在的位置
            Element page = document.getElementsByClass("thumb-listing-page").first();
            Elements elements = page.getElementsByTag("img");
            for (Element element : elements) {
                String attr = element.attr("data-src");
                String replace = replaceFullImg(attr);
//                String[] split = attr.split("/");
//                String fileName = split[split.length - 1];

                System.out.println(replace);
                output.write(replace + "\n");
//                long size = HttpUtil.downloadFile(replace, pathName + fileName);
//                System.out.println(attr + "下载完成。====>SIZE = " + size);
            }
            output.flush();
            output.close();
        } catch (HttpStatusException e) {
            parseIndex(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String replaceFullImg(String attr) {
        String[] smalls = attr.split("small");
        attr = "https://w.wallhaven.cc/full" + smalls[1];
        String[] split = attr.split("/");
        attr = attr.replace(split[split.length - 1], "wallhaven-" + split[split.length - 1]);
        return attr;
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

    private static void testConnect(String uri) {
        // 尝试解决 handshake_failure
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
        Connection connect = Jsoup.connect(uri);
        connect.referrer("https://wallhaven.cc/toplist");
        try {
            System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
            HttpUtil.downloadFile(uri, "E:/file/image/top/1.jpg");
            Document document = connect.get();
            System.out.println(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
