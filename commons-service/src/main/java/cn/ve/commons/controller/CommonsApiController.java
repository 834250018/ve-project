package cn.ve.commons.controller;

import cn.hutool.core.codec.Base64;
import cn.ve.commons.api.CommonsApi;
import cn.ve.commons.pojo.FileParam;
import cn.ve.commons.util.MinioBiz;
import cn.ve.feign.pojo.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@RestController
public class CommonsApiController implements CommonsApi {

    @Resource
    private MinioBiz minioBiz;

    @Override
    public CommonResult<FileParam> getPrivate(String uri) {
        String base64 = minioBiz.getFileBase64(uri);
        FileParam fileParam = new FileParam();
        fileParam.setBase64(base64);
        return CommonResult.success(fileParam);
    }

    @Override
    public CommonResult<String> uploadPrivateAndTagPublic(FileParam param) {
        dealFileParam(param);
        File file;
        try {
            file = Base64.decodeToFile(param.getBase64(), File.createTempFile(param.getPrefix(), param.getSuffix()));
        } catch (IOException e) {
            return CommonResult.fail(400, "解码失败");
        }
        String data = minioBiz.uploadPrivateFile(file);
        HashMap<String, String> map = new HashMap<>();
        map.put(PUBLIC_ACCESS_FILE, PUBLIC_ACCESS_FILE);
        minioBiz.tagFile(data, map);
        return CommonResult.success(data);
    }

    @Override
    public CommonResult<String> putPrivateAndTagPublic(FileParam param) {
        dealFileParam(param);
        File file;
        try {
            file = Base64.decodeToFile(param.getBase64(), File.createTempFile(param.getPrefix(), param.getSuffix()));
        } catch (IOException e) {
            return CommonResult.fail(400, "解码失败");
        }
        String data = minioBiz.coverPrivateFile(file, param.getUri());
        HashMap<String, String> map = new HashMap<>();
        map.put(PUBLIC_ACCESS_FILE, PUBLIC_ACCESS_FILE);
        minioBiz.tagFile(data, map);
        return CommonResult.success(data);
    }

    @Override
    public CommonResult<String> uploadPrivate(FileParam param) {
        dealFileParam(param);
        File file;
        try {
            file = Base64.decodeToFile(param.getBase64(), File.createTempFile(param.getPrefix(), param.getSuffix()));
        } catch (IOException e) {
            return CommonResult.fail(400, "解码失败");
        }
        return CommonResult.success(minioBiz.uploadPrivateFile(file));
    }

    private void dealFileParam(FileParam param) {
        if (StringUtils.isBlank(param.getPrefix())) {
            param.setPrefix("unknown_prefix_");
        }
        if (StringUtils.isBlank(param.getSuffix())) {
            param.setSuffix(".temp");
        }
    }

    @Override
    public CommonResult<String> uploadPublic(FileParam param) {
        File file;
        try {
            file = Base64.decodeToFile(param.getBase64(), File.createTempFile(param.getPrefix(), param.getSuffix()));
        } catch (IOException e) {
            return CommonResult.fail(400, "解码失败");
        }
        return CommonResult.success(minioBiz.uploadPublicFile(file));
    }

    @Override
    public CommonResult<String> getPreSignFile(String uri) {
        if (StringUtils.isBlank(uri)) {
            return CommonResult.success(null);
        }
        try {
            return CommonResult.success(minioBiz.getFile(uri));
        } catch (Exception e) {
            return CommonResult.fail(400, "获取文件异常");
        }
    }

}
