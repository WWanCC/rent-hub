package renthub.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.mapstruct.Mapper;
import renthub.domain.dto.PageResult; // 引入您的PageResult
import renthub.domain.vo.HouseVO;

@Mapper(componentModel = "spring")
public interface PageConverter {

    // 如果源对象和目标对象有相同名称、且类型兼容的属性，则自动进行映射，无需任何 @Mapping 注解。
    // MapStruct 会自动完成所有字段的映射。

//    注意 MapStruct不能使用泛型，每次增加新的转换都需要定义一个新的抽象方法
// 错误示例：<T> PageResult<T> toPageResult(IPage<T> page);
    PageResult<HouseVO> toPageResult(IPage<HouseVO> page);
}