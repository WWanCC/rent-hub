package renthub;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import renthub.domain.po.House;
import renthub.domain.query.PageQuery;
import renthub.mapper.HouseMapper;

import java.util.List;

@SpringBootTest
public class 两步查询 {
    @Autowired
    HouseMapper houseMapper;

    @Test
    public void findListByQuery() {
        PageQuery pageQuery = new PageQuery();
        pageQuery.setRegionId(1);
        List<Integer> listByQuery = houseMapper.findListByQuery(pageQuery);
        System.out.println(listByQuery.toString());
    }
}
