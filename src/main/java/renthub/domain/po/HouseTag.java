package renthub.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class HouseTag {
    @TableField("house_id")
    private Integer houseId;
    @TableField("tag_id")
    private Integer tagId;
}
