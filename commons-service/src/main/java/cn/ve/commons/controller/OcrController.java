package cn.ve.commons.controller;

import cn.ve.base.pojo.VeBaseException;
import cn.ve.base.util.ImgUtil;
import cn.ve.commons.manager.MinioManager;
import cn.ve.commons.pojo.BankCardOCRDTO;
import cn.ve.commons.pojo.IdCardOCRDTO;
import cn.ve.commons.util.OCRUtils;
import cn.ve.feign.pojo.CommonResult;
import cn.ve.rest.util.FileUtil;
import cn.ve.thirdgateway.api.ThirdgatewayApi;
import cn.ve.thirdgateway.pojo.AliOCRParam;
import cn.ve.thirdgateway.pojo.BankCardOcrResp;
import cn.ve.thirdgateway.pojo.IdCardOcrResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.Pattern;
import java.io.File;

/**
 * 公共-文件上传
 *
 * @author ve
 * @date 2021/8/6
 */
@Slf4j
@RequestMapping("/ocr")
@RestController
@Validated
public class OcrController {

    @Resource
    private MinioManager minioManager;

    @Resource
    private ThirdgatewayApi thirdgatewayApi;

    /**
     * (开源)身份证OCR并上传
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/admin/v1/idCardByOS")
    public IdCardOCRDTO uploadIdCard(MultipartFile multipartFile) {
        File tempFile = FileUtil.getFile(multipartFile);
        try {
            IdCardOCRDTO idCardOCRDTO = OCRUtils.idCardOCR(tempFile);
            idCardOCRDTO.setFileUri(minioManager.uploadPrivateFile(tempFile.getName(), tempFile));
            return idCardOCRDTO;
        } catch (Exception e) {
            log.error("开源ocr识别异常: {}", e.getMessage(), e);
            throw new VeBaseException("开源ocr识别异常");
        } finally {
            tempFile.delete();
        }
    }

    /**
     * (开源)银行卡OCR并上传
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/admin/v1/upload/bankCard")
    public BankCardOCRDTO uploadBankCard(MultipartFile multipartFile) {
        File tempFile = FileUtil.getFile(multipartFile);
        try {
            BankCardOCRDTO of = BankCardOCRDTO.of(OCRUtils.bankCardOCR(tempFile));
            of.setFileUri(minioManager.uploadPrivateFile(tempFile.getName(), tempFile));
            return of;
        } catch (Exception e) {
            log.error("开源ocr识别异常: {}", e.getMessage(), e);
            throw new VeBaseException("开源ocr识别异常");
        } finally {
            tempFile.delete();
        }
    }

    /**
     * (阿里)身份证OCR并上传
     *
     * @param multipartFile
     * @param side
     * @param watermarkSkip
     * @return
     */
    @PostMapping("/admin/v1/upload/idCardByAli")
    public IdCardOcrResp uploadIdCardByAli(MultipartFile multipartFile,
        @RequestParam(value = "side", required = false) @Pattern(regexp = "^(face|back)$") String side,
        @RequestParam(value = "watermarkSkip", required = false) Boolean watermarkSkip) {
        if (StringUtils.isBlank(side)) {
            side = "face";
        }
        File tempFile = FileUtil.getFile(multipartFile);

        AliOCRParam aliOCRParam = new AliOCRParam();
        aliOCRParam.setImageBase64(ImgUtil.imgBase64(tempFile.getAbsolutePath()));
        aliOCRParam.setSide(side);
        CommonResult<IdCardOcrResp> idCardOcrResp;
        try {
            idCardOcrResp = thirdgatewayApi.idCardOcr(aliOCRParam);
        } catch (VeBaseException e) {
            log.error("阿里ocr识别异常: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("阿里ocr识别异常: {}", e.getMessage(), e);
            throw new VeBaseException("异常");
        }
        if (idCardOcrResp.getCode() != 200) {
            log.error("阿里ocr识别异常: {}", idCardOcrResp.getMsg());
            throw new VeBaseException("异常");
        }
        IdCardOcrResp data = idCardOcrResp.getData();
        data.setFileUri(minioManager.uploadPrivateFile(tempFile.getName(), tempFile));
        tempFile.delete();
        return data;
    }

    /**
     * (阿里)银行卡OCR并上传
     *
     * @param multipartFile
     * @param watermarkSkip
     * @return
     */
    @PostMapping("/admin/v1/upload/bankCardByAli")
    public BankCardOcrResp uploadBankCardByAli(MultipartFile multipartFile,
        @RequestParam(value = "watermarkSkip", required = false) Boolean watermarkSkip) {
        File tempFile = FileUtil.getFile(multipartFile);

        AliOCRParam aliOCRParam = new AliOCRParam();
        aliOCRParam.setImageBase64(ImgUtil.imgBase64(tempFile.getAbsolutePath()));
        CommonResult<BankCardOcrResp> bankCardOcrResp;
        try {
            bankCardOcrResp = thirdgatewayApi.bankCardOcr(aliOCRParam);
        } catch (Exception e) {
            log.error("阿里ocr识别异常: {}", e.getMessage(), e);
            throw new VeBaseException("异常");
        }
        if (bankCardOcrResp.getCode() != 200) {
            log.error("请求第三方服务异常: {}", bankCardOcrResp.getMsg());
            throw new VeBaseException("服务器异常");
        }
        bankCardOcrResp.getData().setFileUri(minioManager.uploadPrivateFile(tempFile.getName(), tempFile));
        tempFile.delete();
        return bankCardOcrResp.getData();
    }

}