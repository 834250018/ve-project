package cn.ve.commons.config;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ve
 * @date 2019/7/23 22:41
 */
@Configuration
public class FreemarkerConfig {

    @Bean
    public freemarker.template.Configuration freemarkerConfiguration() {
        freemarker.template.Configuration cfg =
            new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_27);
        cfg.setDefaultEncoding("UTF-8");
        // 把异常抛给系统
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(Boolean.FALSE);
        // 谷歌翻译:指定在表达式求值期间或执行自定义指令（和转换）期间抛出的未经检查的异常是否将被包装到TemplateException-s中，或者将按原样冒泡到Template.process（Object，Writer，ObjectWrapper）的调用者
        cfg.setWrapUncheckedExceptions(Boolean.TRUE);
        return cfg;
    }

}