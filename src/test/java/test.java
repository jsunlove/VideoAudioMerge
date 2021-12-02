import com.jsun.pojo.Bilibili;
import com.jsun.util.FfmpegUtil;
import com.jsun.util.FileUtil;
import com.jsun.videoAudioMerge;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 测试方法
 */
public class test {

    private static Logger logger = Logger.getLogger(test.class);

    @Test
    public void testFfmpegUtil() throws IOException {
        String videoInputPath = "D:\\jsun\\video.m4s";
        String audioInputPath = "D:\\jsun\\audio.m4s";
        String videoOutPath = "D:\\jsun\\output.avi";

        System.out.println(new File(videoInputPath).getName());

        FfmpegUtil.merge(videoInputPath, audioInputPath, videoOutPath);
    }

    @Test
    public void testFile() {
        // String path = this.getClass().getResource("config.properties").getPath();
        String path = FileUtil.getPath("config.properties");
        logger.info("地址是："+path);
        Properties properties;
        try {
            properties = new Properties();
            properties.load(new FileInputStream(path));
            logger.info(properties.get("Input.FilesPath"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String path1 = FileUtil.getPath("downloads");
        logger.info(path1+"\\out");

    }


}
