package com.li.spider.controller;

import com.alibaba.fastjson.JSON;
import com.li.spider.common.SpiderBySearch;
import com.li.spider.entity.NovelInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/7/28 13:42
 */
@Controller
public class NovelController {

    @Value("${spider.novel.uri}")
    private String uri;

    @GetMapping("/search")
    public List<NovelInfo> search(@RequestParam(value = "keyWord") String keyWord) {
        List<NovelInfo> novelInfos = new ArrayList<>();
        try {
            Document document = Jsoup.connect(uri + "/search/?ie=gbk&q=" + keyWord).get();
            Elements elements = document.getElementsByClass("s2");
            for (Element element : elements) {
                Elements href = element.getElementsByTag("a");
                Map<String, String> map = new HashMap<>();
                map.put("novelName", href.text());
                map.put("novelUri", uri + href.attr("href"));
                NovelInfo novelInfo = JSON.parseObject(JSON.toJSONString(map), NovelInfo.class);
                novelInfos.add(novelInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return novelInfos;
    }

    @GetMapping("/download")
    public String download(@RequestParam(value = "uri") String uri) {
        try {
            SpiderBySearch.doSpider(uri, "");
            return "html/search-result";
        } catch (Exception e) {
            return "failed";
        }
    }

    @GetMapping("/test")
    public String test() {
        return "html/test";
    }
}
