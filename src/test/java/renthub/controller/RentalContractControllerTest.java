package renthub.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import renthub.auth.StpKit;
import renthub.domain.dto.AdminCreateContractDTO;
import renthub.domain.po.House;
import renthub.domain.po.RentalContract;
import renthub.mapper.HouseMapper;
import renthub.mapper.RentalContractMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // 启用 MockMvc，用于模拟 HTTP 请求
class ContractIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // 模拟 HTTP 请求的工具

    @Autowired
    private ObjectMapper objectMapper; // 用于将 Java 对象序列化为 JSON

    @Autowired
    private HouseMapper houseMapper; // 直接操作数据库，用于准备数据和验证结果

    @Autowired
    private RentalContractMapper rentalContractMapper;

    // --- 在每个测试方法运行前，准备好测试数据 ---
    @BeforeEach
    void setUp() {
        // 清理数据，保证每个测试的独立性 (虽然 ddl-auto 已处理，但手动清理更可靠)
        rentalContractMapper.delete(null);
        houseMapper.delete(null);

        // 准备一套“待出租”的房源
        House house = new House();
        house.setId(1); // 手动设置ID，方便测试
        house.setStatus(1); // 1 = 待出租
        house.setCreatedByEmpId(10); // 假设由10号中介创建
        houseMapper.insert(house);
    }

    @Test
    void testAdminCreateContract_whenSuccess() throws Exception {
        // --- 1. Arrange (准备) ---
        // 准备请求体 DTO
        AdminCreateContractDTO createDto = new AdminCreateContractDTO();
        createDto.setHouseId(1);
        createDto.setUserId(100); // 准备为100号用户创建合同
        createDto.setFinalPrice(new BigDecimal("3500.00"));
        createDto.setStartDate(LocalDate.now().plusDays(1));
        createDto.setEndDate(LocalDate.now().plusMonths(12));

        // 模拟一个登录的中介 (这里我们直接在Service层传入ID，暂不模拟Sa-Token)
        // 在真实项目中，可以使用 @SaTestMock 注解模拟登录

        // --- 2. Act (执行) ---
        // 使用 mockMvc 发起一个 POST 请求
        String responseString = mockMvc.perform(post("/api/admin/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                // --- 3. Assert (断言) ---
                .andExpect(status().isOk()) // 期望 HTTP 状态码为 200
                .andExpect(jsonPath("$.code").value(200)) // 期望返回的 JSON 中 code=200
                .andExpect(jsonPath("$.data").isString()) // 期望返回的 data 是一个字符串 (合同编号)
                .andReturn().getResponse().getContentAsString();

        System.out.println("创建合同成功，响应: " + responseString);

        // --- 4. 验证数据库状态 ---
        // a. 验证房源状态是否被更新为“锁定中” (3)
        House updatedHouse = houseMapper.selectById(1);
        assertThat(updatedHouse.getStatus()).isEqualTo(3);

        // b. 验证合同是否被成功创建
        RentalContract createdContract = rentalContractMapper.selectById(1); // 假设ID从1开始
        assertThat(createdContract).isNotNull();
        assertThat(createdContract.getUserId()).isEqualTo(100);
        assertThat(createdContract.getStatus()).isEqualTo(1); // 1 = 待用户确认
    }

    // ... (在同一个测试类 ContractIntegrationTest.java 中) ...

    @Test
    void testUserConfirmContract_whenSuccess() throws Exception {
        // --- 1. Arrange (准备) ---
        // 除了 @BeforeEach 的房源，我们还需要一份“待确认”的合同
        RentalContract contract = new RentalContract();
        contract.setId(1);
        contract.setHouseId(1);
        contract.setUserId(100); // 假设这是100号用户的合同
        contract.setStatus(1); // 1 = 待用户确认
        rentalContractMapper.insert(contract);

        // 将房源的状态也更新为“锁定中”
        House lockedHouse = houseMapper.selectById(1);
        lockedHouse.setStatus(3); // 3 = 锁定中
        houseMapper.updateById(lockedHouse);

        // 模拟100号用户登录
        // Sa-Token 提供了非常方便的测试工具来模拟登录
        StpUtil.login(100);

        // --- 2. Act (执行) ---
        mockMvc.perform(post("/api/user/contracts/{contractId}/confirm", 1))
                // --- 3. Assert (断言) ---
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // --- 4. 验证数据库状态 ---
        // a. 验证合同状态是否被更新为“进行中” (2)
        RentalContract updatedContract = rentalContractMapper.selectById(1);
        assertThat(updatedContract.getStatus()).isEqualTo(2);

        // b. 验证房源状态是否被更新为“已签约” (2)
        House updatedHouse = houseMapper.selectById(1);
        assertThat(updatedHouse.getStatus()).isEqualTo(2);

        // 清理登录状态，避免影响其他测试
        StpUtil.logout();
    }
}