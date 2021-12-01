package com.jsun.util;

import com.alibaba.fastjson.JSON;
import com.jsun.pojo.Bilibili;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * 文件与文件夹工具类
 *
 * @author sunwenchao-lhq
 * @ClassName fileUtil
 * @date 2021/12/1 10:20
 */
public class FileUtil {

    protected FileUtil() {
    }

    private static Logger logger = Logger.getLogger(FileUtil.class);

    public static Map<String,List<Bilibili>> folderMethod(String path) {
        /*
          --投稿
          ----分p
          ------数据
          --------音频
          --------视频
          --------信息
          ------弹幕库
          ------文件信息
         */
        Map<String,List<Bilibili>> resultMap = new HashMap<>();

        File files = new File(path);

        if (files.exists()) {
            if (null == files.listFiles()) {
                return null;
            }

            LinkedList<File> list = new LinkedList<>(Arrays.asList(Objects.requireNonNull(files.listFiles())));
            while (!list.isEmpty()) {
                // 获取投稿
                File contribution = list.removeFirst();
                // 获取投稿所有分p
                File[] episodes = contribution.listFiles();
                if (null == episodes) {
                    continue;
                }

                List<Bilibili> resultList = new ArrayList<>();
                // 遍历所有的投稿数据
                for (File episode : episodes) {
                    Bilibili bilibili = getData(episode);
                    resultList.add(bilibili);
                }

                resultMap.put(contribution.getName(),resultList);
            }
        } else {
            logger.info("文件不存在!");
        }

        return resultMap;
    }

    /**
     * 从分p中获取数据
     *
     * @param episode 分p数据
     * @return 封装好的数据
     */
    public static Bilibili getData(File episode) {
        Bilibili bilibili = new Bilibili();

        List<File> fileList = getFiles(episode.getPath());
        fileList.forEach(file -> setData(bilibili, file));

        // 如果没有找到源分p名，直接用当前文件代码
        if (StringUtils.isBlank(bilibili.getContributionFileName())) {
            bilibili.setContributionFileName(episode.getName());
        }

        return bilibili;
    }


    public static List<File> getFiles(String filePath) {
        List<File> fileList = new ArrayList<>();

        Path start = FileSystems.getDefault().getPath(filePath);
        try {
            Files.walk(start).filter(path -> path.toFile().isFile())
                    .forEach(path -> fileList.add(path.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    /**
     * 判断文件类型，获取封装数据
     *
     * @param bilibili
     * @param file
     */
    private static void setData(Bilibili bilibili, File file) {

        switch (file.getName()) {
            case "entry.json":
                bilibili.setContributionFileName(getContributionFileName(file));
                break;
            case "danmuku.xml":
                bilibili.setDanmuku(file.getAbsolutePath());
                break;
            case "video.m4s":
                bilibili.setVideo(file.getAbsolutePath());
                break;
            case "audio.m4s":
                bilibili.setAudio(file.getAbsolutePath());
                break;
            default:
                break;
        }
    }

    /**
     * 通过entry.json获取投稿文件名
     *
     * @param jsonFile entry.json的文件对象
     * @return 投保文件名
     */
    public static String getContributionFileName(File jsonFile) {

        String jsonStr = null;
        try {
            jsonStr = readJsonFile(jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            String title = JSON.parseObject(jsonStr).getString("title");
            logger.info("title:" + title);
            String part = JSON.parseObject(jsonStr).getJSONObject("page_data").getString("part");
            logger.info("part:" + part);
            String downloadSubtitle = JSON.parseObject(jsonStr).getJSONObject("page_data").getString("download_subtitle");
            logger.info("download_subtitle:" + downloadSubtitle);

            if(title.trim().equalsIgnoreCase(downloadSubtitle.trim())){
                return title;
            } else {
                return part;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 读取json文件
     *
     * @param jsonFile file对象
     * @return 返回json字符串
     */
    public static String readJsonFile(File jsonFile) throws IOException {
        String jsonStr = "";


        FileReader fileReader = null;
        Reader reader = null;
        try {
            fileReader = new FileReader(jsonFile);
            reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }


    public static String folderFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null != files) {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        folderFiles(file2.getAbsolutePath());
                    } else {
                        return file2.getAbsolutePath();
                    }
                }
            }
        }

        return null;
    }


}
