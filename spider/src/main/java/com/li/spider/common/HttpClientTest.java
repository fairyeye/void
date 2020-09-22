package com.li.spider.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HttpClientTest {
    private static Long COUNT = 0L;
    
    public static void main(String[] args) {
        String uri = "https://www.biquge.lu/book/525";
//        String uri = "https://www.biquge.lu/book/525/7766056.html";
        doSpider(uri);
    }

    private static void doSpider(String uri) {
        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet(uri);
        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);

            //4.判断响应状态为200，进行处理
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");

                /**
                 * 下面是Jsoup展现自我的平台
                 */
                //6.Jsoup解析html
                Document document = Jsoup.parse(html);
                //像js一样，通过标签获取title
//                System.out.println(document.getElementsByTag("title").first());
                if (!uri.contains(".html")) {
                    //像js一样，通过id 获取文章列表元素对象
                    Elements listmain = document.getElementsByClass("listmain");
                    Elements dd = listmain.first().getElementsByTag("dd");
                    for (Element element : dd) {
                        String href = element.getElementsByTag("a").attr("href");
//                        doSpider("https://www.biquge.lu" + href);
                        System.out.println(element.text());
//                        System.out.println(href);
                    }
                } else {
//                    Elements tag = document.getElementsByTag("h1");
//                    String content = document.getElementById("content").text();
//                    BufferedWriter output = new BufferedWriter(new FileWriter("E:/file/wmsj.txt",true));
//                    output.write(tag.text());
//                    output.write(content);
//                    output.flush();
//                    output.close();
                }
//                //像js一样，通过class 获取列表下的所有博客
//                Elements postItems = postList.getElementsByClass("href");
//                //循环处理每篇博客
//                for (Element postItem : postItems) {
//                    //像jquery选择器一样，获取文章标题元素
//                    Elements titleEle = postItem.select(".post_item_body a[class='titlelnk']");
//                    System.out.println("文章标题:" + titleEle.text());;
//                    System.out.println("文章地址:" + titleEle.attr("href"));
//                    //像jquery选择器一样，获取文章作者元素
//                    Elements footEle = postItem.select(".post_item_foot a[class='lightblue']");
//                    System.out.println("文章作者:" + footEle.text());;
//                    System.out.println("作者主页:" + footEle.attr("href"));
//                    System.out.println("*********************************");
//                }
//                System.out.println(html);
            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }



}