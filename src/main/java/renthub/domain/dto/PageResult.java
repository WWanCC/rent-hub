package renthub.domain.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    /**
     * 当前页码
     */
    private long current;
    /**
     * 每页条数
     */
    private long size;
    /**
     * 符合条件的总条数
     */
    private long total;

    /**
     * 当前页数据列表
     */
    private List<T> records;

    //备用转换方法，现在使用Mapstruct转换，来避免手动转换
    public static <T> PageResult<T>
    from(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setRecords(page.getRecords());
        return result;
    }
}