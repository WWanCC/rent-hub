package renthub.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户收藏表
 * </p>
 *
 * @author Bai5
 * @since 2025-07-01
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_favorite")
public class UserFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表 user.id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 房源表 house.id
     */
    @TableField("house_id")
    private Integer houseId;

    @TableField("created_at")
    private LocalDateTime createdAt;


}
