package com.li.spider.common;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/7/27 17:00
 */
public class Spider {
    public static void main(String[] args) {
        String uri = "https://www.biquge.lu/book/43597/32993794.html/";
        doSpider(uri, "");
    }

    private static void doSpider(String uri, String path) {
        Boolean flag = false;
        Boolean repeatFlag = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
            if (StringUtils.isEmpty(path)) {
                path = "E:/file/" + sdf.format(new Date()) + ".txt";
            }
            Document document = Jsoup.connect(uri).get();
            if (!uri.contains(".html")) {
                Element title = document.getElementsByTag("title").first();
                if (Objects.nonNull(title)) {
                    path = "E:/file/" + title.text() + ".txt";
                }
                Elements listmain = document.getElementsByClass("listmain");
                Elements dd = listmain.first().getElementsByTag("dd");
                for (Element element : dd) {
                    Elements href = element.getElementsByTag("a");
                    if (href.text().contains("第一章")) {
                        flag = true;
                        if (repeatFlag) {
                            File file = new File(path);
                            if (file.exists()) {
                                file.renameTo(new File("E:/file/bak.txt"));
                            }
                        }
                        repeatFlag = true;
                    }
                    if (flag) {
                        doSpider("https://www.biquge.lu" + href.attr("href"), path);
                    }
                }
                System.out.println(title.text() + "下载完成");
            } else {
                Elements tag = document.getElementsByTag("h1");
                Element content = document.getElementById("content");
                BufferedWriter output = new BufferedWriter(new FileWriter(path, true));
                if (Objects.nonNull(tag)) {
                    if (StringUtils.isEmpty(tag.text())) {
//                        System.out.println("读取失败，重试中...");
//                        doSpider(uri, path);
//                        return;
                        System.out.println(uri + "读取失败...");
                    }
                    output.write(tag.text());
                    System.out.println(tag.text() + "开始下载...");
                }
                if (Objects.nonNull(content)) {
                    output.write(content.text());
                }
                output.flush();
                output.close();
            }
        } catch (HttpStatusException e) {
            System.err.println(uri + "访问失败，重试中...");
            doSpider(uri, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
