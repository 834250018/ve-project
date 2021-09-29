package cn.ve.user.controller.admin;

import cn.hutool.http.Header;

import java.nio.charset.StandardCharsets;

import cn.ve.base.util.BeanUtils;
import cn.ve.base.util.CsvUtils;
import cn.ve.base.util.StringConstant;
import cn.ve.user.dto.UserLoginDTO;
import cn.ve.user.dal.entity.UserLogin;
import cn.ve.user.param.UserLoginCreateForm;
import cn.ve.user.param.UserLoginUpdateForm;
import cn.ve.user.param.UserLoginUpdateStatusForm;
import cn.ve.user.param.UserLoginQO;
import cn.ve.user.service.UserLoginService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/userLogin")
@Api(value = "后台-登陆表", description = "后台-登陆表")
public class UserLoginAdminController {

    @Autowired
    private UserLoginService userLoginService;

    @ApiOperation(value = "查询单条数据")
    @GetMapping("/v1.0/one")
    public UserLoginDTO queryById(@RequestParam Long id) {
        return BeanUtils.copy(userLoginService.queryById(id), UserLoginDTO.class);
    }

    @ApiOperation(value = "查询分页")
    @GetMapping("/v1.0/page")
    public PageInfo<UserLoginDTO> queryPage(@ModelAttribute @Valid UserLoginQO qo) {
        return PageInfo.of(BeanUtils.copyList(userLoginService.queryAll(qo), UserLoginDTO.class));
    }

    private static final String[] TITLES = new String[]{"主键", "名称"};
    private static final String[] PROPERTIES = new String[]{"id", "name"};
    private static final Map<String, Function<String, String>> FUNCTION_MAP =
            new HashMap<String, Function<String, String>>() {{
                put("id", value -> {
                    if (Integer.parseInt(value) == 1) {
                        return "a";
                    } else {
                        return value;
                    }
                });
            }};

    @ApiOperation(value = "导出当前查询条件下的所有数据,最多1w条")
    @GetMapping("/v1.0/exportCsv")
    public void exportCsv(HttpServletResponse response, @ModelAttribute @Valid UserLoginQO qo) throws Exception {
        response.setHeader(Header.CONTENT_ENCODING.getValue(), StandardCharsets.UTF_8.displayName());
        response.setHeader(Header.CONTENT_DISPOSITION.getValue(),
                StringConstant.CONTENT_DISPOSITION_VALUE_PREFIX + System.currentTimeMillis() + ".csv");
        CsvUtils<UserLoginDTO> csvUtils = new CsvUtils<>(response.getOutputStream(), TITLES, PROPERTIES, FUNCTION_MAP, UserLoginDTO.class);
        csvUtils.writeData(() -> BeanUtils.copyList(userLoginService.queryAll(qo), UserLoginDTO.class), qo, 10);
    }

    @ApiOperation(value = "新增一条数据")
    @PostMapping("/v1.0/insertOne")
    public int insert(@RequestBody @Valid UserLoginCreateForm form) {
        return userLoginService.insert(BeanUtils.copy(form, UserLogin.class));
    }

    @ApiOperation(value = "修改一条数据")
    @PostMapping("/v1.0/updateOne")
    public int update(@RequestBody @Valid UserLoginUpdateForm form) {
        return userLoginService.update(BeanUtils.copy(form, UserLogin.class));
    }

    @ApiOperation(value = "修改状态")
    @PostMapping("/v1.0/updateStatus")
    public void update(@RequestBody @Valid UserLoginUpdateStatusForm form) {
        userLoginService.updateStatus(form.getId(), form.getStatus());
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping("/v1.0/remove")
    public int deleteById(@RequestParam Long id) {
        return userLoginService.deleteById(id);
    }

}

