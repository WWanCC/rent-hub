package renthub.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StringTrimDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, IOException {
        // 获取原始的文本值
        String value = p.getValueAsString();
        // 如果值不为 null，则调用 trim() 方法去除首尾空格
        return (value != null) ? value.trim() : null;
    }
}
