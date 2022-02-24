package cn.ve.thirdgateway.controller;

import cn.ve.rest.util.FileUtil;
import cn.ve.thirdgateway.pojo.BaiduFaceMatchResp;
import cn.ve.thirdgateway.util.BaiduFaceUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author ve
 * @date 2021/8/6
 */
@Slf4j
@RequestMapping
@RestController
@Validated
public class BaiduController {

    @Value("${baidu.ak}")
    private String ak;
    @Value("${baidu.sk}")
    private String sk;

    @ApiOperation(value = "(百度)人脸比对,分值不低于80视为同一个人")
    @PostMapping("/baidu/v1/upload/faceMatch")
    public BaiduFaceMatchResp faceMatch(MultipartFile face1, MultipartFile face2) {
        File tempFile1 = FileUtil.getFile(face1);
        File tempFile2 = FileUtil.getFile(face2);
        BaiduFaceMatchResp baiduFaceMatchResp =
            new BaiduFaceUtils(ak, sk)
                .faceMatch0(tempFile1.getAbsolutePath(), tempFile2.getAbsolutePath(), "BASE64");
        tempFile1.delete();
        tempFile2.delete();
        return baiduFaceMatchResp;
    }

}