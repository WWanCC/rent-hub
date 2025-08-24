package renthub.utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource; // 用于从 classpath 加载资源
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AliOssUtilIntegrationTest {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Test
    void testUploadRealImage() throws IOException {
        // --- 1. 从测试资源目录加载真实图片文件 ---

        // a. 定义要加载的图片在 resources 目录下的相对路径
        String imagePath = "五笔字根表.jpg";

        // b. 使用 Spring 的 ClassPathResource 来定位文件
        ClassPathResource resource = new ClassPathResource(imagePath);

        // c. 断言文件确实存在，这是一个好习惯，避免因路径错误导致测试失败
        assertTrue(resource.exists(), "测试图片不存在于 src/test/resources/ 目录下: " + imagePath);

        // --- 2. 将加载的文件转换成 MultipartFile ---

        MultipartFile multipartFile;
        // try-with-resources 确保输入流被自动关闭
        try (InputStream inputStream = resource.getInputStream()) {
            multipartFile = new MockMultipartFile(
                    "file",                             // 参数名，可以任意填写
                    resource.getFilename(),             // 使用文件的原始名称
                    Files.probeContentType(resource.getFile().toPath()), // 自动探测文件的MIME类型, e.g., "image/jpeg"
                    inputStream                         // 文件的输入流
            );
        }

        // 打印文件信息，方便调试
        System.out.println("准备上传文件: " + multipartFile.getOriginalFilename());
        System.out.println("文件大小: " + multipartFile.getSize() + " bytes");
        System.out.println("文件类型: " + multipartFile.getContentType());

        // --- 3. 调用 upload 方法，执行真实上传 ---

        String fileUrl = aliOssUtil.upload(multipartFile, "integration-test-images");

        // --- 4. 打印并断言结果 ---

        System.out.println("文件上传成功！请在浏览器中打开以下URL进行验证:");
        System.out.println(fileUrl);

        assertNotNull(fileUrl, "返回的URL不应为空");
        assertTrue(fileUrl.startsWith("https://rent-hub.oss-cn-beijing.aliyuncs.com"), "URL前缀不正确");
        assertTrue(fileUrl.contains("/integration-test-images/"), "业务目录不正确");
        assertTrue(fileUrl.endsWith(".jpg"), "文件扩展名不正确");
    }
}