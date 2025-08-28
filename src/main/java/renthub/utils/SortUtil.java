package renthub.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SortUtil {

    // 缓存 Key: Class, Value: 该实体所有合法数据库列名的Set
    private final Map<Class<?>, Set<String>> validColumnsCache = new ConcurrentHashMap<>();

    // !!! 新增一个缓存，专门存放每个实体类的主键列名 !!!
    // Key: Class, Value: 该实体的主键列名 (e.g., "id")
    private final Map<Class<?>, String> primaryKeyCache = new ConcurrentHashMap<>();

    public <T> void applySort(QueryWrapper<T> wrapper, Class<T> entityClass, String sortField, String sortOrder) {
        Set<String> validColumns = getValidSortColumns(entityClass);
        String sortColumn = StringUtils.camelToUnderline(sortField);

        if (StringUtils.isNotBlank(sortColumn) && validColumns.contains(sortColumn)) {
            // 条件1：如果前端传入了合法的、非空的排序字段，则使用它
            boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
            wrapper.orderBy(true, isAsc, sortColumn);
        }
        else {
            // 条件2：否则，应用默认排序
            // --- 核心改动：不再硬编码 "created_at" ---
            String defaultSortColumn = getDefaultSortColumn(entityClass);
            if (StringUtils.isNotBlank(defaultSortColumn)) {
                wrapper.orderByDesc(defaultSortColumn);
            }
        }
    }

    public Set<String> getValidSortColumns(Class<?> entityClass) {
        return validColumnsCache.computeIfAbsent(entityClass, this::parseAndCacheColumns);
    }

    /**
     * 【新增】获取一个实体类的默认排序列名。
     * 优先使用 "created_at"，如果不存在，则回退到使用主键。
     *
     * @param entityClass 实体类的 Class 对象
     * @return 可用的默认排序列名，如果都没有则返回 null
     */
    private String getDefaultSortColumn(Class<?> entityClass) {
        // 先确保元数据已被解析和缓存
        Set<String> validColumns = getValidSortColumns(entityClass);

        // 优先使用 created_at
        if (validColumns.contains("created_at")) {
            return "created_at";
        }

        // 如果没有 created_at，则回退到使用主键
        // 主键信息也从缓存中获取
        return primaryKeyCache.get(entityClass);
    }

    /**
     * 【新增】一个集中的方法，用于解析实体类的元数据，并同时缓存字段白名单和主键
     *
     * @param clazz 实体类的 Class 对象
     * @return 字段白名单 Set
     */
    private Set<String> parseAndCacheColumns(Class<?> clazz) {
        System.out.println("首次加载 " + clazz.getSimpleName() + " 的元数据并缓存...");
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if (tableInfo == null) {
            primaryKeyCache.put(clazz, null); // 缓存空值，避免重复解析
            return Set.of();
        }

        // 缓存主键
        if (StringUtils.isNotBlank(tableInfo.getKeyColumn())) {
            primaryKeyCache.put(clazz, tableInfo.getKeyColumn());
        }

        // 缓存所有字段
        Set<String> columns = tableInfo.getFieldList().stream()
                .map(TableFieldInfo::getColumn)
                .collect(Collectors.toSet());
        if (StringUtils.isNotBlank(tableInfo.getKeyColumn())) {
            columns.add(tableInfo.getKeyColumn());
        }
        return columns;
    }
}


//package renthub.utils; // 建议统一放在 util 包下
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
//import com.baomidou.mybatisplus.core.metadata.TableInfo;
//import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import org.springframework.stereotype.Component;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
/// **
// * MyBatis-Plus 通用排序工具类
// * 实现了基于动态白名单的安全排序逻辑，可被项目中任何 Service 复用。
// */
//@Component
//public class SortUtil {
//
//    /**
//     * 缓存每个实体类对应的“字段白名单”。
//     * Key: 实体类的Class对象 (e.g., RentalContract.class)
//     * Value: 该实体所有合法数据库列名的Set集合
//     * 使用 ConcurrentHashMap 保证线程安全。
//     */
//    private final Map<Class<?>, Set<String>> validColumnsCache = new ConcurrentHashMap<>();
//
//    /**
//     * 为 QueryWrapper 动态、安全地应用排序条件。
//     *
//     * @param wrapper       需要应用排序的 QueryWrapper 实例。
//     * @param entityClass   当前操作的实体类的 Class 对象 (e.g., RentalContract.class)。
//     * @param sortField     前端传入的排序字段 (驼峰式, e.g., "finalPrice")。
//     * @param sortOrder     前端传入的排序方向 (e.g., "asc" or "desc")。
//     * @param <T>           泛型，代表实体类的类型。
//     */
//    public <T> void applySort(QueryWrapper<T> wrapper, Class<T> entityClass, String sortField, String sortOrder) {
//        // 1. 从缓存中获取或动态生成该实体类的字段白名单
//        Set<String> validColumns = getValidSortColumns(entityClass);
//
//        // 2. 将前端传入的驼峰命名安全地转换为数据库的下划线命名
//        String sortColumn = StringUtils.camelToUnderline(sortField);
//
//        // 3. 核心安全校验：检查转换后的列名是否存在于白名单中
//        if (validColumns.contains(sortColumn)) {
//            // 如果存在，则是安全的，可以用于排序
//            boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
//            wrapper.orderBy(true, isAsc, sortColumn);
//        } else {
//            // 如果不存在，则应用默认排序规则，以保证程序的健壮性
//            // 检查白名单中是否有 "created_at" 这个通用字段，有才应用
//            if (validColumns.contains("created_at")) {
//                wrapper.orderByDesc("created_at");
//            }
//        }
//    }
//
//    /**
//     * 私有辅助方法：获取指定实体类的所有合法数据库列名。
//     * 内部实现了缓存逻辑，只有在首次请求某个实体类时才会解析其元数据。
//     *
//     * @param entityClass 实体类的 Class 对象。
//     * @return 包含所有数据库列名的 Set 集合。
//     */
//    private Set<String> getValidSortColumns(Class<?> entityClass) {
//        // computeIfAbsent 是一个原子操作，保证了在并发环境下也只计算一次
//        return validColumnsCache.computeIfAbsent(entityClass, clazz -> {
//            // 打印日志，方便观察缓存是否生效
//            System.out.println("首次加载 " + clazz.getSimpleName() + " 的字段白名单并缓存...");
//
//            TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
//            if (tableInfo == null) {
//                // 如果传入的 Class 不是一个有效的 MyBatis-Plus 实体，返回一个空集合
//                return Set.of();
//            }
//
//            // 从表信息中，获取所有字段的数据库列名，并收集到 Set 中
//            Set<String> columns = tableInfo.getFieldList().stream()
//                    .map(TableFieldInfo::getColumn)
//                    .collect(Collectors.toSet());
//
//            // (可选，但推荐) 将主键也加入到可排序字段中
//            if (StringUtils.isNotBlank(tableInfo.getKeyColumn())) {
//                columns.add(tableInfo.getKeyColumn());
//            }
//            return columns;
//        });
//    }
//}