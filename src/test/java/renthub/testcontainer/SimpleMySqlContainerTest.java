package renthub.testcontainer;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import renthub.domain.po.User;
import renthub.mapper.UserMapper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


//这个案例的测试失败了，因为缺少了合适的初始化建表schema.sql以及要把h2的作用域修改，避免冲突


// 1. 声明这是一个 Spring Boot 集成测试，会加载完整的应用上下文
@SpringBootTest
// 2. 激活 Testcontainers 的 JUnit 5 扩展
@Testcontainers
@DisplayName("Testcontainers 集成测试：验证 MySQL 容器与 UserMapper")
class SimpleMySqlContainerTest {

    // 3. 定义一个 MySQL 容器
    // @Container 注解会告诉 Testcontainers 管理这个容器的生命周期（启动和销毁）
    // 'static' 关键字让这个容器在所有测试方法之间共享，只启动一次，效率更高
    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.28"));

    // 注意：因为你在 pom.xml 中正确添加了 "spring-boot-testcontainers" 依赖，
    // 所以你不再需要手动编写 @DynamicPropertySource 方法来注入URL、用户名和密码。
    // Spring Boot 的自动配置会帮你完成这一切！

    // 4. 像往常一样，直接注入你需要测试的组件
    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("应该能成功向由 Testcontainers 启动的数据库中插入并查询用户")
    void shouldInsertAndSelectUserSuccessfully() {
        // Arrange (准备)
        // 创建一个实体类，但这里我们用一个 Map 来模拟，避免你需要创建实体
        // 实际项目中你应该 new 一个 User 对象
        User user = new User(1, "手机号码", "密码", LocalDateTime.now(), LocalDateTime.now(), 1111);

        // Act (执行)
        int insertedRows = userMapper.insert(user);

        // Assert (断言)
        assertThat(insertedRows).isEqualTo(1); // 确认插入了一行

        // 从数据库中把刚刚插入的数据查回来
        User foundUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", "test-from-container"));

        // 验证查回来的数据是否正确
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isNotNull(); // 确认ID已自动生成
        assertThat(foundUser.getPhone()).isEqualTo("12345678901");
    }
}