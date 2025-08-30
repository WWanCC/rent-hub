package renthub.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import renthub.domain.po.*;

import java.util.Map;

@Configuration
public class GraphQLConfig {

    // 1. 创建一个静态的、不可变的类型映射注册表
    private static final Map<Class<?>, String> TYPE_MAPPING = Map.of(
            House.class, "House",
            RentalContract.class, "RentalContract",
            User.class, "User",
            Region.class, "Region",
            Emp.class, "Emp",
            Tag.class, "Tag",
            Notification.class, "Notification"
    );

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.Json) // 注册 JSON // 绑定 JSON 的实现
                .scalar(ExtendedScalars.GraphQLLong) // <-- // 绑定 Long 的实现
                // 2. 这里的 typeResolver() 会自动引用下面那个同名 Bean
                // --- 【核心修正】为 EntityResult 联合类型注册一个 TypeResolver ---
                .type("EntityResult", typeWiring -> typeWiring
                        .typeResolver(entityResultTypeResolver())
                );
    }


    @Bean
    public TypeResolver entityResultTypeResolver() {
        // 3. 实现一个极其简洁的、基于 Map 查找的 TypeResolver
        return env -> {
            Object javaObject = env.getObject();
            String typeName = TYPE_MAPPING.get(javaObject.getClass());
            if (typeName != null) {
                return env.getSchema().getObjectType(typeName);
            }
            return null;
        };
    }

//    /**
//     * 【新增】一个 Bean，专门用于解析 EntityResult 的具体类型
//     */
//    @Bean
//    public TypeResolver entityResultTypeResolver() {
//        return env -> {
//            // env.getObject() 返回的是后端 DataFetcher 返回的那个 Java 对象
//            Object javaObject = env.getObject();
//
//            // 根据 Java 对象的 class，返回其在 Schema 中对应的 GraphQL 类型名
//            if (javaObject instanceof House) {
//                return env.getSchema().getObjectType("House");
//            }
//            if (javaObject instanceof RentalContract) {
//                return env.getSchema().getObjectType("RentalContract");
//            }
//            if (javaObject instanceof User) {
//                return env.getSchema().getObjectType("User");
//            }
//            if (javaObject instanceof Region) {
//                return env.getSchema().getObjectType("Region");
//            }
//            if (javaObject instanceof Emp) {
//                return env.getSchema().getObjectType("Emp");
//            }
//            if (javaObject instanceof Tag) {
//                return env.getSchema().getObjectType("Tag");
//            }
//            if (javaObject instanceof Notification) {
//                return env.getSchema().getObjectType("Notification");
//            }
//
//            // 如果都不是，返回 null，GraphQL 会抛出错误
//            return null;
//        };
//    }


}