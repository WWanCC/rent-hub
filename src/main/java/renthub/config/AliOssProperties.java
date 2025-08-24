package renthub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 用于从git忽略的AliOssSecretConfig中读取密钥和ID
 */

@Component
@ConfigurationProperties(prefix = "aliyun.oss") //找到yml等配置文件中所有以 aliyun.oss 开头的配置项，然后把它们的值，自动地、批量地 注入到这个 Java 类的同名字段中
public class AliOssProperties {

    private String endpoint;
    private String bucketName;

    // --- 必须提供 getter 和 setter ---
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getBucketName() { return bucketName; }
    public void setBucketName(String bucketName) { this.bucketName = bucketName; }
}