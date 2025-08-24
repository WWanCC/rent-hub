package renthub.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import renthub.config.AliOssProperties; // 导入属性类

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class AliOssUtil {

    private final OSS ossClient;
    private final AliOssProperties aliOssProperties;

    // 通过构造函数，让 Spring 把创建好的 Bean 注入进来
    public AliOssUtil(OSS ossClient, AliOssProperties aliOssProperties) {
        this.ossClient = ossClient;
        this.aliOssProperties = aliOssProperties;
    }

    // 文件上传方法 uuid配合文件夹分层
    public String upload(MultipartFile file, String businessType) throws IOException {
        String bucketName = aliOssProperties.getBucketName();
        String endpoint = aliOssProperties.getEndpoint();

        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String uniqueFilename = uuid + "." + extension;
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String objectKey = businessType + "/" + datePath + "/" + uniqueFilename;

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, inputStream);
            this.ossClient.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("文件上传到OSS失败", e);
        }

        return "https://" + bucketName + "." + endpoint + "/" + objectKey;
    }
}