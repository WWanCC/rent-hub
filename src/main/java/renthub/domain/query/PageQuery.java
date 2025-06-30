package renthub.domain.query;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true) // 声明比较父类属性
public class PageQuery extends PageParam {
    @Min(value = 1, message = "无效regionId")
    private Integer regionId;
    @Size(max = 30, message = "搜索关键字过长")
    private String keyword;
    @Size(max = 4, message = "最多选择4种户型")
    private List<Integer> layout; //户型 房间数
    private List<Integer> tag;
    @DecimalMin(value = "0.0", message = "最低租金不能为负数")
    private BigDecimal minRent;
    @DecimalMin(value = "0.0", message = "最高租金不能为负数")
    private BigDecimal maxRent;
    private String sorted = "desc";
    private Integer tagSize;  //用于记录tag标签数量,直接给XML使用 (前端不使用)

    // 重写 setTag 方法,配合tagSize给XML取值使用
    public void setTag(List<Integer> tag) {
        this.tag = tag;
        if (tag != null) {
            this.tagSize = tag.size();
        } else {
            this.tagSize = 0;
        }
    }
}
