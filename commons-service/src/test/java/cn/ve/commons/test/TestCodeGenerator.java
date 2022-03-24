/*
package cn.ve.commons.test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Cleanup;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * @author ve
 * @date 2019/7/24 13:29
 *//*

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class TestCodeGenerator {

    public static final String RESOURCES_PATH = "src/main/resources/templates";

    @Resource
    Configuration freemarkerConfiguration;

    public void write(File source, File target, Map<String, String> params) throws IOException, TemplateException {
        freemarkerConfiguration.setDirectoryForTemplateLoading(source.getParentFile());
        Template temp = freemarkerConfiguration.getTemplate(source.getName());
        @Cleanup FileWriter writer = new FileWriter(target);
        temp.process(params, writer);
    }

    @Test
    public void test() {
        String group = "com.ywy";
        String projectName = "code_generator";
        // 要生成的Entry对象:例如:String name = "people" 会在当前目录下生成一个PeopleEntry
        String name = "user";
        File currentDir = new File("src/main/java/com/ywy/code_generator/test");

        // 模板文件
        File souce = new File(RESOURCES_PATH, "Entry.java.ftl");
        // 生成目标文件
        File target = new File(currentDir, StringUtils.capitalize(name) + "Entry.java");
        Map<String, String> params = new HashMap<>();
        params.put("group", group);
        params.put("projectName", projectName);
        params.put("name", name);
        try {
            write(souce, target, params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}*/
