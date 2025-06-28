package renthub;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import renthub.domain.po.House;
import renthub.domain.query.PageQuery;
import renthub.mapper.HouseMapper;

@SpringBootTest
public class 两步查询 {
    @Autowired
    HouseMapper houseMapper;

    @Test
    public void findListByQuery() {
//        IPage<House> page = new Page<>(1, 10);
        PageQuery pageQuery = new PageQuery();
        pageQuery.setRegionId(1);
        Integer listByQuery = houseMapper.findListByQuery(pageQuery);
        System.out.println(listByQuery);
    }
}
