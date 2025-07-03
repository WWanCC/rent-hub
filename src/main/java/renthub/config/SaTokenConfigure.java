package renthub.config;
@Configuration
public class SaTokenConfigure {
    // Sa-Token 整合 jwt (Mixin 混入模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForMixin();
    }
}
