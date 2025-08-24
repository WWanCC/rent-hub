package renthub.common;

import org.springframework.http.ResponseEntity;
import renthub.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import renthub.domain.dto.Result;

import java.io.IOException;

@RestController
@RequestMapping("/api/common") //
public class UploadController {

    private final AliOssUtil aliOssUtil;

    // 构造函数注入
    public UploadController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }

    /**
     * 通用图片上传接口
     *
     * @param file 上传的图片文件
     * @return 包含图片URL的Result对象
     * @throws IOException
     */
    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            // 在实际项目中，应返回一个包含错误信息的 Result 对象
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 调用我们已经封装好的工具类，将图片上传到 "listings" 目录下
        String imageUrl = aliOssUtil.upload(file, "listings");

        // 将成功后的URL返回给前端
        return ResponseEntity.ok(imageUrl);
    }
}