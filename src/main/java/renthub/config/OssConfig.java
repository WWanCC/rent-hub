package renthub.config; // 确保包名正确

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

    /**
     * @param aliOssProperties Spring会自动将我们上一步创建的属性对象注入进来
     */
    @Bean(destroyMethod = "shutdown")
    public OSS ossClient(AliOssProperties aliOssProperties) {
        // 从属性类获取非敏感信息
        String endpoint = aliOssProperties.getEndpoint();

        // 从被忽略的密钥文件中获取敏感信息
        String accessKeyId = AliOssSecretConfig.ACCESS_KEY_ID;
        String accessKeySecret = AliOssSecretConfig.ACCESS_KEY_SECRET;

        // 创建并返回 OSSClient 实例
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}