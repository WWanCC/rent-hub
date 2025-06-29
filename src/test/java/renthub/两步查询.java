package renthub;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import renthub.domain.po.House;
import renthub.domain.query.PageQuery;
import renthub.mapper.HouseMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class 两步查询 {
    @Autowired
    HouseMapper houseMapper;

    @Test //第一步查询
    public void findListByQuery() {
        IPage<House> page = new Page<>(1, 2);
        PageQuery pageQuery = new PageQuery();
        pageQuery.setRegionId(1);

//        pageQuery.setTag(List.of(4, 6));
//        pageQuery.setTagSize(pageQuery.getTag().size());

//        pageQuery.setLayout(List.of(1,2));

//        pageQuery.setMinRent(BigDecimal.valueOf(2300));
//        pageQuery.setMaxRent(BigDecimal.valueOf(3500));

//        pageQuery.setKeyword("东圃");
//        pageQuery.setKeyword("整租");

//        pageQuery.setSorted("asc");
//        System.out.println(pageQuery.getSorted());
        List<Integer> listByQuery = houseMapper.findListByQuery(page, pageQuery);
        System.out.println(listByQuery.toString());
    }
}
