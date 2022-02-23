package cn.ve.commons.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.ve.base.pojo.VeException;
import cn.ve.commons.api.CommonsApi;
import cn.ve.commons.util.MinioBiz;
import io.minio.messages.Tags;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.Date;

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

    @Autowired
    private MinioBiz minioBiz;

    //    @Autowired
    //    private AliApi aliApi;

    @ApiOperation(value = "定时清理临时文件夹")
    @PostMapping("/admin/v1/upload/clear")
    public void clear() {
        minioBiz.clearTempBucket();
    }

    @ApiOperation(value = "上传文件到临时bucket,定期清理")
    @PostMapping("/admin/v1/upload/temp")
    public String uploadTempFile(MultipartFile multipartFile,
        @RequestParam(value = "watermarkSkip", required = false) Boolean watermarkSkip) {
//        return minioBiz.uploadTempFile(multipartFile, watermarkSkip);
        return null;
    }

    @ApiOperation(value = "上传文件到公共bucket")
    @PostMapping("/admin/v1/upload/public")
    public String uploadPublicFile(MultipartFile multipartFile,
        @RequestParam(value = "watermarkSkip", required = false) Boolean watermarkSkip) {
//        return minioBiz.uploadPublicFile(multipartFile, watermarkSkip);
        return null;
    }

    @ApiOperation(value = "上传文件到私有bucket,后续走服务验证完权限后获取文件")
    @PostMapping("/admin/v1/upload/private")
    public String uploadPrivateFile(MultipartFile multipartFile,
        @RequestParam(value = "watermarkSkip", required = false) Boolean watermarkSkip) {
//        return minioBiz.uploadPrivateFile(multipartFile, watermarkSkip);
        return null;
    }

    @ApiOperation(value = "通过文件uri获取私有文件,uri为文件目录+文件名+后缀,以二进制的形式返回文件")
    @GetMapping("/admin/v1/getFile")
    public void getFile(@RequestParam String uri, HttpServletResponse response) {
        minioBiz.getFile(uri, response);
    }

    @ApiOperation(value = "通过文件uri获取私有文件,uri为文件目录+文件名+后缀,以临时url的形式下载文件,一小时有效")
    @GetMapping("/admin/v1/getPreSignFile")
    public String getPreSignFile(@RequestParam String uri) {
        return minioBiz.getFile(uri);
    }

    //    @AuthIgnore
    @ApiOperation(value = "通过文件uri获取含公共访问标签的私有文件,uri为文件目录+文件名+后缀,以二进制的形式返回文件")
    @GetMapping("/admin/v1.1.3/getFileWithTag/{date}/{fileName}")
    public void getFileWithTag(@PathVariable String fileName, @PathVariable String date, HttpServletResponse response) {
        String uri = date + "/" + fileName;
        Tags fileTags;
        try {
            fileTags = minioBiz.getFileTags(uri);
        } catch (Exception e) {
            throw new VeException(403, "无权访问");
        }
        if (!CommonsApi.PUBLIC_ACCESS_FILE.equals(fileTags.get().get(CommonsApi.PUBLIC_ACCESS_FILE))) {
            throw new VeException(403, "无权访问");
        }
        minioBiz.getFile(uri, fileName, response);
    }

    @ApiOperation(value = "通过文件uri获取私有文件,uri为文件目录+文件名+后缀,以二进制的形式返回文件")
    @GetMapping("/admin/v1.1.3/getPrivateFile/{date}/{fileName}")
    public void getPrivateFile(@PathVariable String fileName, @PathVariable String date, HttpServletResponse response) {
        String uri = date + "/" + fileName;
        Tags fileTags;
        try {
            fileTags = minioBiz.getFileTags(uri);
        } catch (Exception e) {
            throw new VeException(403, "无权访问");
        }
        if (!CommonsApi.PUBLIC_ACCESS_FILE.equals(fileTags.get().get(CommonsApi.PUBLIC_ACCESS_FILE))) {
            throw new VeException(403, "无权访问");
        }
        minioBiz.getFile(uri, fileName, response);
    }
/*

    @ApiOperation(value = "(开源)身份证OCR并上传")
    @PostMapping("/admin/v1/upload/idCard")
    public IdCardDTO uploadIdCard(MultipartFile multipartFile,
        @RequestParam(value = "watermarkSkip", required = false) Boolean watermarkSkip) {
        File tempFile;
        try {
            tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error("FileUploadController_uploadIdCard:{}" + e.getMessage(), e);
            throw new VeException(CommonsServiceStatusCode.FILE_UPLOAD_FAIL.getCode(),
                CommonsServiceStatusCode.FILE_UPLOAD_FAIL.getMsg());
        }
        try {
            IdCardDTO from = IdCardDTO.from(OCRUtils.idCardOCR(tempFile));
            from.setIdCardUri(minioBiz.uploadPrivateFile(tempFile, watermarkSkip));
            return from;
        } catch (Exception e) {
            log.error("自研ocr识别异常: {}", e.getMessage(), e);
            throw new VeException(CommonsServiceStatusCode.OCR_FAIL.getCode(),
                CommonsServiceStatusCode.OCR_FAIL.getMsg());
        } finally {
            tempFile.delete();
        }
    }

    @ApiOperation(value = "(开源)银行卡OCR并上传")
    @PostMapping("/admin/v1/upload/bankCard")
    public BankCardOCRDTO uploadBankCard(MultipartFile multipartFile,
        @RequestParam(value = "watermarkSkip", required = false) Boolean watermarkSkip) {
        File tempFile;
        try {
            tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error("FileUploadController_uploadIdCard:{}" + e.getMessage(), e);
            throw new VeException(CommonsServiceStatusCode.FILE_UPLOAD_FAIL.getCode(),
                CommonsServiceStatusCode.FILE_UPLOAD_FAIL.getMsg());
        }
        try {
            BankCardOCRDTO of = BankCardOCRDTO.of(OCRUtils.bankCardOCR(tempFile));
            of.setBankCardUri(minioBiz.uploadPrivateFile(tempFile, watermarkSkip));
            return of;
        } catch (Exception e) {
            log.error("自研ocr识别异常: {}", e.getMessage(), e);
            throw new VeException(CommonsServiceStatusCode.OCR_FAIL.getCode(),
                CommonsServiceStatusCode.OCR_FAIL.getMsg());
        } finally {
            tempFile.delete();
        }
    }

    private static final String yyyyMMdd = "yyyyMMdd";

    @ApiImplicitParams(@ApiImplicitParam(required = true, name = "side", value = "身份证正反面类型 face:正面(人像面) back反面(国徽面)", paramType = "query"))
    @ApiOperation(value = "(阿里)身份证OCR并上传")
    @PostMapping("/admin/v1/upload/idCardByAli")
    public IdCardDTO uploadIdCardByAli(MultipartFile multipartFile,
        @RequestParam(value = "side", required = false) @Pattern(regexp = "^(face|back)$") String side,
        @RequestParam(value = "watermarkSkip", required = false) Boolean watermarkSkip) {
        if (StringUtils.isBlank(side)) {
            side = "face";
        }
        File tempFile;
        try {
            tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error("FileUploadController_uploadIdCard:{}" + e.getMessage(), e);
            throw new VeException(CommonsServiceStatusCode.FILE_UPLOAD_FAIL.getCode(),
                CommonsServiceStatusCode.FILE_UPLOAD_FAIL.getMsg());
        }
        AliParam aliParam = new AliParam();
        aliParam.setImageBase64(ImgUtils.imgBase64(tempFile.getAbsolutePath()));
        aliParam.setSide(side);
        CommonResult<IdCardOcrResp> idCardOcrResp;
        try {
            idCardOcrResp = aliApi.idCardOcr(aliParam);
        } catch (VeException e) {
            log.error("阿里ocr识别异常: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("阿里ocr识别异常: {}", e.getMessage(), e);
            throw new VeException(CommonsServiceStatusCode.OCR_FAIL.getCode(),
                CommonsServiceStatusCode.OCR_FAIL.getMsg());
        }
        if (idCardOcrResp.getCode() != 200) {
            log.error("阿里ocr识别异常: {}", idCardOcrResp.getMsg());
            throw new VeException(CommonsServiceStatusCode.OCR_FAIL.getCode(),
                CommonsServiceStatusCode.OCR_FAIL.getMsg());
        }
        IdCardOcrResp ocr = idCardOcrResp.getData();
        IdCardDTO from = new IdCardDTO();
        if ("face".equals(side)) {
            from.setRealName(ocr.getName());
            from.setIdNo(ocr.getNum());
            from.setGender(ocr.getSex());
            from.setNation(ocr.getNationality());
            long age = DateUtil.betweenYear(DateTime.of(ocr.getBirth(), yyyyMMdd).toJdkDate(), new Date(), false);
            from.setAge((int)age);
            from.setBirth(ocr.getBirth());
            from.setAddress(ocr.getAddress());
            from.setIdCardUri(minioBiz.uploadPrivateFile(tempFile, watermarkSkip));
        } else {
            from.setStartDate(DateTime.of(ocr.getStart_date(), yyyyMMdd).toString("yyyy-MM-dd"));
            from.setEndDate(DateTime.of(ocr.getEnd_date(), yyyyMMdd).toString("yyyy-MM-dd"));
            from.setIssue(ocr.getIssue());
            from.setIdCardUri(minioBiz.uploadPrivateFile(tempFile, watermarkSkip));
        }
        tempFile.delete();
        return from;
    }

    @ApiOperation(value = "(阿里)银行卡OCR并上传")
    @PostMapping("/admin/v1/upload/bankCardByAli")
    public BankCardOCRDTO uploadBankCardByAli(MultipartFile multipartFile,
        @RequestParam(value = "watermarkSkip", required = false) Boolean watermarkSkip) {
        File tempFile;
        try {
            tempFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".png");
            multipartFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error("FileUploadController_uploadIdCard:{}" + e.getMessage(), e);
            throw new VeException(CommonsServiceStatusCode.FILE_UPLOAD_FAIL.getCode(),
                CommonsServiceStatusCode.FILE_UPLOAD_FAIL.getMsg());
        }
        AliParam aliParam = new AliParam();
        aliParam.setImageBase64(ImgUtils.imgBase64(tempFile.getAbsolutePath()));
        BankCardOcrResp bankCardOcrResp;
        try {
            bankCardOcrResp = aliApi.bankCardOcr(aliParam);
        } catch (Exception e) {
            log.error("阿里ocr识别异常: {}", e.getMessage(), e);
            throw new VeException(CommonsServiceStatusCode.OCR_FAIL.getCode(),
                CommonsServiceStatusCode.OCR_FAIL.getMsg());
        }
        BankCardOCRDTO of = new BankCardOCRDTO();
        of.setBankName(bankCardOcrResp.getBank_name());
        of.setBankCardNo(bankCardOcrResp.getCard_num());
        of.setBankCardUri(minioBiz.uploadPrivateFile(tempFile, watermarkSkip));
        tempFile.delete();
        return of;
    }
*/

}