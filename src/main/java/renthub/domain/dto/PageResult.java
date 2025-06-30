package renthub.domain.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {

    /**
     * 当前页数据列表
     */
    private List<T> records;
    /**
     * 总记录数
     */
    private long total;
    /**
     * 每页条数
     */
    private long size;
    /**
     * 当前页码
     */
    private long current;

    public static <T> PageResult<T> from(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setRecords(page.getRecords());
        return result;
    }
}