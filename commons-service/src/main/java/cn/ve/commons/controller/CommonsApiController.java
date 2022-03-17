package cn.ve.commons.controller;

import cn.hutool.core.codec.Base64;
import cn.ve.base.util.IdUtil;
import cn.ve.commons.api.CommonsApi;
import cn.ve.commons.manager.MinioManager;
import cn.ve.commons.pojo.FileParam;
import cn.ve.feign.pojo.CommonResult;
import cn.ve.file.util.MinioUtil;
import cn.ve.rest.util.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;

@RestController
public class CommonsApiController implements CommonsApi {

    @Resource
    private MinioManager minioManager;

    @Override
    public CommonResult<String> getPrivate(String uri) {
        String base64 = minioManager.getFileBase64(MinioUtil.PRIVATE_BUCKET, uri);
        return CommonResult.success(base64);
    }

    @Override
    public CommonResult<String> uploadPrivate(FileParam param) {
        File file = Base64.decodeToFile(param.getBase64(), FileUtil.genTempFile());
        String data = minioManager.uploadPrivateFile(IdUtil.genUUID(), file);
        // 如果要标记私有文件为可访问,加个标签 map.put(PUBLIC_ACCESS_FILE, PUBLIC_ACCESS_FILE);
        if (CollectionUtils.isNotEmpty(param.getTags())) {
            minioManager.tagFile(MinioUtil.PRIVATE_BUCKET, data, param.getTags());
        }
        return CommonResult.success(data);
    }

    @Override
    public CommonResult<String> uploadPublic(FileParam param) {
        File file = Base64.decodeToFile(param.getBase64(), FileUtil.genTempFile());
        return CommonResult.success(minioManager.uploadPublicFile("fileName", file));
    }

    @Override
    public CommonResult<String> getPreSignFile(String uri) {
        try {
            return CommonResult.success(minioManager.getPreSignFileUrl(MinioUtil.PRIVATE_BUCKET, uri));
        } catch (Exception e) {
            return CommonResult.fail(400, "获取文件异常");
        }
    }

}
