package renthub.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import renthub.service.IRentalContractService;

import java.time.LocalDateTime;

/**
 * 合同相关的定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContractStatusTask {

    // 注入我们需要的 Service
    private final IRentalContractService contractService;



    /**
     * 位置	含义	允许的值	常用特殊字符
     * 1. 秒	秒	0-59	* (每秒), , (和), - (范围), / (步长)
     * 2. 分	分钟	0-59	(同上)
     * 3. 时	小时	0-23	(同上)
     * 4. 日	一个月中的第几天	1-31	? (不指定), L (最后一天), W (工作日)
     * 5. 月	月份	1-12 或 JAN-DEC	(同上)
     * 6. 周	一周中的第几天	1-7 或 SUN-SAT	?, L, # (第几个周几)
     * (7. 年)	年份 (可选)	1970-2099	(同上)
     */

    /**
     * 【核心任务】每天凌晨 2:00 执行，扫描即将到期的合同并发送通知。
     * cron 表达式的含义将在下面详细讲解。
     */
//    @Scheduled(cron = "0 0 2 * * ?")

    //【临时添加】改为每 10 秒执行一次，方便测试
    @Scheduled(initialDelay = 5000, fixedRate = 10000) // 启动后延迟5秒开始，然后每10秒执行一次
    public void checkContractsAndSendNotifications() {
        log.info("定时任务开始：扫描即将到期的合同... 当前时间: {}", LocalDateTime.now());

        try {
            // 将复杂的业务逻辑委托给 Service 层去处理
            contractService.processExpiringContracts();

            log.info("定时任务成功结束：扫描即将到期的合同。");
        } catch (Exception e) {
            // 捕获所有异常，防止一个任务的失败影响到其他定时任务的执行
            log.error("定时任务[扫描即将到期的合同]执行失败！", e);
        }
    }

    /**
     * 【可选】合同状态自动更新任务
     * 每天凌晨 1:00 执行，将已到期的合同状态置为“已结束”。
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateExpiredContractsStatus() {
        log.info("定时任务开始：更新已到期合同状态...");
        try {
            contractService.processFinishedContracts();
        } catch (Exception e) {
            log.error("定时任务[更新已到期合同状态]执行失败！", e);
        }
    }
}