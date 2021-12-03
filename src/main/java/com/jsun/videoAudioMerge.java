package com.jsun;

import com.jsun.pojo.Bilibili;
import com.jsun.util.FfmpegUtil;
import com.jsun.util.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 主类
 */
public class videoAudioMerge {

    private static Logger logger = Logger.getLogger(videoAudioMerge.class);


    public static void main(String[] args) {
        work();
    }

    public static void work() {
        // 读取配置文件，获取文件地址和输出地址
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config");

        String inputFilesPath = resourceBundle.getString("Input.FilesPath");
        String outputVideoPath = resourceBundle.getString("Output.VideoPath");

        String[] list = new File(inputFilesPath).list();

        if (outputVideoPath.endsWith("\\")) {
            outputVideoPath = outputVideoPath + "out\\";
        } else {
            outputVideoPath = outputVideoPath + "\\out\\";
        }

        long startTime = System.currentTimeMillis();   //获取开始时间

        // 获取文件数据
        List<Bilibili> bilibilis = FileUtil.folderMethod(inputFilesPath);

        String finalOutputVideoPath = outputVideoPath;
        bilibilis.forEach(bilibili -> {
            // 生成文件目录
            boolean mkdirs = new File(finalOutputVideoPath + bilibili.getTitle()).mkdirs();
            logger.info("文件夹生成结果:" + mkdirs);

            String videoInputPath = bilibili.getVideo();
            String audioInputPath = bilibili.getAudio();
            String videoOutPath;
            if (StringUtils.isBlank(bilibili.getTitle())) {
                logger.info("不是缓存数据：" + bilibili.toString());
                // 不是缓存数据跳出本次循环
                return;
            }
            String outputVideoFormat = resourceBundle.getString("Output.VideoFormat");

            if (StringUtils.isBlank(outputVideoFormat)) {
                outputVideoFormat = "avi";
            }

            if (bilibili.getTitle().equalsIgnoreCase(bilibili.getContributionFileName())) {
                videoOutPath = finalOutputVideoPath + bilibili.getTitle() + "\\" + bilibili.getContributionFileName() + "."+outputVideoFormat;
            } else {
                videoOutPath = finalOutputVideoPath + bilibili.getTitle() + "\\P" + bilibili.getPage() + "-" + bilibili.getContributionFileName() + "." + outputVideoFormat;
            }

            try {
                FfmpegUtil.merge(videoInputPath, audioInputPath, videoOutPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        long endTime = System.currentTimeMillis(); //获取结束时间

        logger.info("程序运行时间： " + (endTime - startTime) + "ms");
    }

}
