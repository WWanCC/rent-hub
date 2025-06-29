package renthub.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

//可以处理Get请求 URL传值
@ControllerAdvice // 声明这是一个全局控制器建言类
public class GlobalDataBinderConfig {

    /**
     * 定义一个全局的初始化绑定器。
     * 这将在所有@RequestMapping处理方法执行前被调用。
     * @param binder 数据绑定器
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 注册一个用于String类型的编辑器
        // StringTrimmerEditor是Spring自带的一个属性编辑器，用于去除字符串两端的空白
        // 构造函数的布尔值参数true表示：如果trim后的字符串为空（""），则将其转换为null
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}