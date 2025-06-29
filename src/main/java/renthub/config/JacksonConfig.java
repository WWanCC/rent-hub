package renthub.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.deserializerByType(String.class, new StringTrimDeserializer());
    }//为 `Jackson2ObjectMapperBuilder` 添加一个自定义配置，指定当遇到 `String.class`，使用`StringTrimDeserializer` 实例来进行反序列化。
}
