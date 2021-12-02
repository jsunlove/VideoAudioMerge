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

import static org.apache.log4j.helpers.Loader.getResource;

/**
 * 文件与文件夹工具类
 *
 */
public class FileUtil {

    protected FileUtil() {
    }

    private static Logger logger = Logger.getLogger(FileUtil.class);

    public static List<Bilibili> folderMethod(String path) {
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

        List<Bilibili> resultList = new ArrayList<>();
        File files = new File(path);

        if (files.exists()) {
            if (null == files.listFiles()) {
                return Collections.emptyList();
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

                // 遍历所有的投稿数据
                for (File episode : episodes) {
                    Bilibili bilibili = getData(episode);
                    resultList.add(bilibili);
                }
            }
        } else {
            logger.info("文件不存在!");
        }

        return resultList;
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
                bilibili.setContributionFileName(getName(2,file));
                bilibili.setTitle(getName(1,file));
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
     * 通过entry.json获取投稿文件名，分p名
     *
     * @param type 类型 1-投稿名  2-分p名
     * @param jsonFile entry.json的文件对象
     * @return 投保文件名
     */
    public static String getName(int type, File jsonFile) {

        String jsonStr;
        try {
            jsonStr = readJsonFile(jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            // 投稿名
            String title = JSON.parseObject(jsonStr).getString("title");
            // 分p名
            String part = JSON.parseObject(jsonStr).getJSONObject("page_data").getString("part");
            // 下载名 （投稿名 分p名）
            String downloadSubtitle = JSON.parseObject(jsonStr).getJSONObject("page_data").getString("download_subtitle");

            if(type == 1){
                return title;
            } else if (type == 2) {
                if(title.trim().equalsIgnoreCase(downloadSubtitle.trim())){
                    return title;
                } else {
                    return part;
                }
            }

            return null;
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


    /**
     * 获取当前项目路径
     * @return
     */
    public static String getPath(String fileName){
        // return FileUtil.class.getResource(fileName).getFile();
        return Class.class.getResource(fileName).getFile();
    }

}
