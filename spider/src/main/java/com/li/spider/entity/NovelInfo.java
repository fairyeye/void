package com.li.spider.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/7/28 14:05
 */
@Setter
@Getter
public class NovelInfo implements Serializable {

    private static final long serialVersionUID = 4115426736537752510L;

//    private Long id;
    private String novelName;
    private String novelUri;
}
