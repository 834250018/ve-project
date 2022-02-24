package cn.ve.commons.controller;

import cn.hutool.core.date.DateTime;
import cn.ve.base.pojo.VeException;
import cn.ve.base.util.StringConstant;
import cn.ve.commons.api.CommonsApi;
import cn.ve.commons.manager.MinioManager;
import cn.ve.file.util.MinioUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author ve
 * @date 2021/8/6
 */
@Slf4j
@Api(value = "公共-文件上传", description = "公共-文件上传接口")
@RequestMapping("/file")
@RestController
@Validated
public class FileUploadController {

    @Resource
    private MinioManager minioManager;

    @ApiOperation(value = "上传文件到临时bucket,定期清理")
    @PostMapping("/admin/v1/upload/temp")
    public String uploadTempFile(MultipartFile multipartFile) {
        try {
            return minioManager.uploadPublicFile(
                MinioUtil.TEMP_PATH + DateTime.now().toString(MinioUtil.CATALOG_DATE_FORMATTER) + StringConstant.SLASH
                    + multipartFile.getName(), multipartFile.getInputStream());
        } catch (IOException e) {
            throw new VeException("获取文件异常");
        }
    }

    @ApiOperation(value = "上传文件到公共bucket")
    @PostMapping("/admin/v1/upload/public")
    public String uploadPublicFile(MultipartFile multipartFile) {
        try {
            return minioManager.uploadPublicFile(
                DateTime.now().toString(MinioUtil.CATALOG_DATE_FORMATTER) + StringConstant.SLASH + multipartFile
                    .getName(), multipartFile.getInputStream());
        } catch (IOException e) {
            throw new VeException("获取文件异常");
        }
    }

    @ApiOperation(value = "上传文件到私有bucket")
    @PostMapping("/admin/v1/upload/private")
    public String uploadPrivateFile(MultipartFile multipartFile) {
        try {
            return minioManager.uploadPrivateFile(
                DateTime.now().toString(MinioUtil.CATALOG_DATE_FORMATTER) + StringConstant.SLASH + multipartFile
                    .getName(), multipartFile.getInputStream());
        } catch (IOException e) {
            throw new VeException("获取文件异常");
        }
    }

    @ApiOperation(value = "通过文件uri获取私有文件,走下载操作")
    @GetMapping("/admin/v1/getFile")
    public void getFile(@RequestParam String uri, HttpServletResponse response) {
        minioManager.getFile(MinioUtil.PRIVATE_BUCKET, uri, response);
    }

    @ApiOperation(value = "通过文件uri获取私有文件,返回临时下载url,一小时有效")
    @GetMapping("/admin/v1/getPreSignFile")
    public String getPreSignFile(@RequestParam String uri) {
        return minioManager.getPreSignFileUrl(MinioUtil.PRIVATE_BUCKET, uri);
    }

    //    @AuthIgnore
    @ApiOperation(value = "通过文件uri获取含公共访问标签的私有文件,走下载操作")
    @GetMapping("/admin/v1.0/getFileWithTag/{date}/{fileName}")
    public void getFileWithTag(@PathVariable String fileName, @PathVariable String date, HttpServletResponse response) {
        String uri = date + "/" + fileName;
        Map<String, String> fileTags;
        try {
            fileTags = minioManager.getFileTags(MinioUtil.PRIVATE_BUCKET, uri);
        } catch (Exception e) {
            throw new VeException(403, "无权访问");
        }
        if (!CommonsApi.PUBLIC_ACCESS_FILE.equals(fileTags.get(CommonsApi.PUBLIC_ACCESS_FILE))) {
            throw new VeException(403, "无权访问");
        }
        minioManager.getFile(uri, fileName, response);
    }

    @ApiOperation(value = "通过文件uri获取私有文件,走下载操作")
    @GetMapping("/admin/v1.0/getPrivateFile/{date}/{fileName}")
    public void getPrivateFile(@PathVariable String fileName, @PathVariable String date, HttpServletResponse response) {
        String uri = date + "/" + fileName;
        Map<String, String> fileTags;
        try {
            fileTags = minioManager.getFileTags(MinioUtil.PRIVATE_BUCKET, uri);
        } catch (Exception e) {
            throw new VeException(403, "无权访问");
        }
        if (!CommonsApi.PUBLIC_ACCESS_FILE.equals(fileTags.get(CommonsApi.PUBLIC_ACCESS_FILE))) {
            throw new VeException(403, "无权访问");
        }
        minioManager.getFile(uri, fileName, response);
    }

}