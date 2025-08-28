package renthub.auth.strategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 通用查询的鉴权策略接口。
 * 每一个实现了此接口的类，都代表了一种针对特定数据表的权限检查方案。
 * @param <T> 该策略所针对的实体类型
 */
public interface QueryAuthStrategy<T> {

    /**
     * 【我是谁？】
     * 这个方法用来声明：“我这个安检员，是专门负责检查哪个实体类（数据表）的”。
     * 比如 ContractQueryAuthStrategy 就会返回 RentalContract.class。
     *
     * @return 该策略支持的实体类的 Class 对象。
     */
    Class<T> getEntityClass();

    /**
     * 【我要做什么？】
     * 这是策略的核心方法，是真正的“安检”动作。
     * 它接收一个正在构建中的 QueryWrapper，并可以在上面添加强制的、
     * 基于当前用户身份的过滤条件，从而实现数据级别的权限控制。
     *
     * @param wrapper 需要被修改的 QueryWrapper，用于添加权限过滤条件。
     */
    void applyAuth(QueryWrapper<T> wrapper);
}