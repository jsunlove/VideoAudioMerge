package com.jsun;

import com.jsun.pojo.Bilibili;
import com.jsun.util.FfmpegUtil;
import com.jsun.util.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 主类
 */
public class videoAudioMerge {

    private static Logger logger = Logger.getLogger(videoAudioMerge.class);

    @Test
    public void main() {

        // 读取配置文件，获取文件地址和输出地址
        Properties properties = null;
        try {
            properties.load(new FileInputStream(FileUtil.getPath("config.properties")));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("程序报错", e);
        }

        assert properties != null;
        String inputFilesPath = (String) properties.get("Input.FilesPath");
        String outputVideoPath = (String) properties.get("Output.VideoPath");

        if (StringUtils.isBlank(inputFilesPath) || new File(inputFilesPath).listFiles().length <= 0) {
            inputFilesPath = FileUtil.getPath( "downloads");
        }

        if (StringUtils.isBlank(outputVideoPath)) {
            outputVideoPath = FileUtil.getPath( "downloads");
            if(outputVideoPath.endsWith("\\")){
                outputVideoPath = outputVideoPath+"out\\";
            }else{
                outputVideoPath = outputVideoPath+"\\out\\";
            }
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
            String videoOutPath = finalOutputVideoPath + bilibili.getTitle() + "\\" + bilibili.getContributionFileName() + ".avi";

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
