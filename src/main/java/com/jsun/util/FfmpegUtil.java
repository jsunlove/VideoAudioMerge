package com.jsun.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * ffmpeg工具类
 *
 * @author sunwenchao-lhq
 * @ClassName ffmpegUtil
 * @date 2021/12/1 9:54
 */
public class FfmpegUtil {


    private static Logger logger = Logger.getLogger(FfmpegUtil.class);

    protected FfmpegUtil() {
    }

    public static void merge(String videoInputPath, String audioInputPath, String videoOutPath) throws IOException {
        StringBuilder ffmpeg = new StringBuilder();
        ffmpeg.append("ffmpeg -i ");
        ffmpeg.append(videoInputPath);
        ffmpeg.append(" -i ");
        ffmpeg.append(audioInputPath);
        ffmpeg.append(" -c:v copy -c:a aac -strict experimental");
        ffmpeg.append(" -map 0:v:0 -map 1:a:0 -y ");
        ffmpeg.append(videoOutPath);

        Process process = null;

        InputStream errorStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;

        try {
            process = Runtime.getRuntime().exec(ffmpeg.toString());

            errorStream = process.getErrorStream();
            inputStreamReader = new InputStreamReader(errorStream);
            br = new BufferedReader(inputStreamReader);

            // 用来收集错误信息的
            String str = "";
            while ((str = br.readLine()) != null) {
                logger.info(str);
            }

            process.waitFor();

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            if (br != null) {
                br.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (errorStream != null) {
                errorStream.close();
            }
        }

    }
}
