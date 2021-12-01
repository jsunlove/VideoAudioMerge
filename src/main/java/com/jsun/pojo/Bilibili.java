package com.jsun.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * bilibili缓存文件实体类
 *
 * @author sunwenchao-lhq
 * @ClassName Bilibili
 * @date 2021/12/1 10:43
 */
@Getter
@Setter
@ToString
public class Bilibili {

    // 投稿文件名
    private String contributionFileName;

    // danmuku.xml
    private String danmuku;

    // audio.m4s
    private String audio;

    // video.m4s
    private String video;

}
