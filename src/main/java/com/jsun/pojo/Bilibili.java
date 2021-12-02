package com.jsun.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * bilibili缓存文件实体类
 *
 */
@Getter
@Setter
@ToString
public class Bilibili {

    // 投稿稿件名
    private String title;

    // 投稿分p名
    private String contributionFileName;

    // danmuku.xml地址
    private String danmuku;

    // audio.m4s 地址
    private String audio;

    // video.m4s 地址
    private String video;

}
