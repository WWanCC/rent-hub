package renthub.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.mapstruct.Mapper;
import renthub.domain.dto.PageResult; // 引入您的PageResult

@Mapper(componentModel = "spring")
public interface PageConverter {

    // 如果源对象和目标对象有相同名称、且类型兼容的属性，则自动进行映射，无需任何 @Mapping 注解。
    // MapStruct 会自动完成所有字段的映射。
    <T> PageResult<T> toPageResult(IPage<T> page);

}