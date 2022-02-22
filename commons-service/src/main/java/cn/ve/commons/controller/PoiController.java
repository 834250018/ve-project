package cn.ve.commons.controller;

import cn.ve.commons.util.PoiUtil;
//import cn.ve.user.api.UserApi;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ve
 * @date 2018/10/15 17:00
 */
@Slf4j
@RestController
@Api(value = "poi-controller", tags = "表格")
@Validated
public class PoiController {

    @GetMapping(value = "/read")
    public List<List<Object>> readExcel(@NotBlank @RequestParam("url") String url) throws IOException {
        File file = new File(url);
        return PoiUtil.readExcel(file);
    }

    /**
     * 导出excel.xlsx
     *
     * @param res
     * @throws IOException
     */
    @GetMapping(value = "/export")
    public void export(HttpServletResponse res) throws IOException {
        // demo data
        List<List<Object>> arrayLists = new ArrayList<>();
        arrayLists.add(Stream.of("number", "name", "age").collect(Collectors.toList()));
        arrayLists.add(Stream.of("1", "张三", 25).collect(Collectors.toList()));
        arrayLists.add(Stream.of("2", "李四", 30).collect(Collectors.toList()));
        PoiUtil.writeIntoResponse(res, arrayLists);
    }

}