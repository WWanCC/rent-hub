package renthub.service.impl;

import renthub.po.House;
import renthub.mapper.HouseMapper;
import renthub.service.HouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 房源 服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-06-25
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

}
