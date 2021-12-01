import com.jsun.pojo.Bilibili;
import com.jsun.util.FfmpegUtil;
import com.jsun.util.FileUtil;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 测试方法
 *
 * @author sunwenchao-lhq
 * @ClassName test
 * @date 2021/12/1 10:23
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
    public void testFileUtil() {
        String path = "C:\\Users\\sunwenchao-lhq\\Desktop\\ffmpeg";
        // List<Bilibili> list = FileUtil.folderMethod1(path);
        System.out.println(FileUtil.folderFiles(path));
    }

    @Test
    public void jsun() throws IOException {

        String filePath = "C:\\Users\\sunwenchao-lhq\\Desktop\\ffmpeg";
        Path start = FileSystems.getDefault().getPath(filePath);
        Files.walk(start).filter(path -> path.toFile().isFile())
                .forEach(path -> System.out.println(path.toFile().getAbsolutePath()));
    }

    @Test
    public void testGetData() {
        Bilibili bilibili = FileUtil.getData(new File("C:\\Users\\sunwenchao-lhq\\Desktop\\ffmpeg\\3637095\\c_5818388"));
        System.out.println("结果" + bilibili);
    }

    @Test
    public void testFolderMethod() {
        String path = "C:\\Users\\sunwenchao-lhq\\Desktop\\downloads";
        FileUtil.folderMethod(path);
    }


}
