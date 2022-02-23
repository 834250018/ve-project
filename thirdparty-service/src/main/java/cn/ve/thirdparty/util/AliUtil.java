package cn.ve.thirdparty.util;

import cn.ve.thirdparty.pojo.BankCard3FactorResp;
import cn.ve.thirdparty.pojo.BankCardOcrResp;
import cn.ve.thirdparty.pojo.IdCardOcrResp;
import cn.ve.thirdparty.pojo.OcrReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ve
 * @date 2021/8/31
 */
@Slf4j
public class AliUtil {

    private static final String BANK_CARD_OCR_URL_PREFIX = "https://yhk.market.alicloudapi.com";
    private static final String BANK_CARD_OCR_URL_SUFFIX = "/rest/160601/ocr/ocr_bank_card.json";
    private static final String ID_CARD_OCR_URL_PREFIX = "https://dm-51.data.aliyun.com";
    private static final String ID_CARD_OCR_URL_SUFFIX = "/rest/160601/ocr/ocr_idcard.json";
    private static final String BANK_CARD_3_FACTOR_URL_PREFIX = "https://bankcard3c.shumaidata.com";
    private static final String BANK_CARD_3_FACTOR_URL_SUFFIX = "/bankcard3c";
    private final String appKey;
    private final String appSecret;
    private static final String HMAC_SHA256 = "HmacSHA256";
    private final long curTime;
    private final String curTimeStr;

    public AliUtil(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        Date curDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        curTime = curDate.getTime();
        curTimeStr = dateFormat.format(curDate);
    }

    public IdCardOcrResp idCardOcr(String imageBase64, String side) {
        OcrReq req = new OcrReq();
        req.setImage(imageBase64);
        req.getConfigure().setSide(side);
        HttpMethod method = HttpMethod.POST;
        ResponseEntity<IdCardOcrResp> exchange = new RestTemplate()
            .exchange(ID_CARD_OCR_URL_PREFIX + ID_CARD_OCR_URL_SUFFIX, method,
                new HttpEntity<>(req, getAuthHeaders(method, ID_CARD_OCR_URL_SUFFIX)), IdCardOcrResp.class);
        return exchange.getBody();
    }

    private String getSign(String stringToSign) {
        Mac hmacSha256;
        try {
            hmacSha256 = Mac.getInstance(HMAC_SHA256);
        } catch (NoSuchAlgorithmException e) {
            log.error("没有找到此算法: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        byte[] keyBytes = appSecret.getBytes(StandardCharsets.UTF_8);
        try {
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, HMAC_SHA256));
        } catch (InvalidKeyException e) {
            log.error("密钥初始化失败: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        byte[] md5Result = hmacSha256.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(md5Result), StandardCharsets.UTF_8);
    }

    public BankCardOcrResp bankCardOcr(String imageBase64) {
        RestTemplate restTemplate = new RestTemplate();
        OcrReq req = new OcrReq();
        req.setImage(imageBase64);
        HttpMethod method = HttpMethod.POST;
        ResponseEntity<BankCardOcrResp> exchange = restTemplate
            .exchange(BANK_CARD_OCR_URL_PREFIX + BANK_CARD_OCR_URL_SUFFIX, method,
                new HttpEntity<>(req, getAuthHeaders(method, BANK_CARD_OCR_URL_SUFFIX)), BankCardOcrResp.class);
        return exchange.getBody();
    }

    public BankCard3FactorResp bankCard3FactorAuth(String bankcard, String idcard, String name) {
        RestTemplate restTemplate = new RestTemplate();
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("bankcard", bankcard);
        uriVariables.put("idcard", idcard);
        uriVariables.put("name", name);
        HttpMethod method = HttpMethod.GET;
        ResponseEntity<BankCard3FactorResp> exchange;
        try {
            exchange = restTemplate
                .exchange(BANK_CARD_3_FACTOR_URL_PREFIX + BANK_CARD_3_FACTOR_URL_SUFFIX + buildRequestUrl(uriVariables),
                    method, new HttpEntity<>(
                        getAuthHeaders(method, BANK_CARD_3_FACTOR_URL_SUFFIX + buildRequestUrl(uriVariables))),
                    BankCard3FactorResp.class);
        } catch (Exception e) {
            log.error("银联三要素异常:{}", e.getMessage());
            log.error("响应体内容: {}", ((HttpClientErrorException.BadRequest)e).getResponseBodyAsString());
            BankCard3FactorResp bankCard3FactorResp = new BankCard3FactorResp();
            bankCard3FactorResp.setCode(400);
            bankCard3FactorResp.setMsg("银行卡校验失败");
            bankCard3FactorResp.setSuccess(Boolean.FALSE);
            return bankCard3FactorResp;
        }
        return exchange.getBody();
    }

    public static String buildRequestUrl(Map<String, String> params) {
        StringBuilder url = new StringBuilder("?");
        List<String> list = new ArrayList<>();
        for (String key : params.keySet()) {
            list.add(key + "=" + params.get(key) + "&");
        }
        list.sort(String::compareTo);
        for (String s : list) {
            url.append(s);
        }
        return url.substring(0, url.length() - 1);
    }

    private HttpHeaders getAuthHeaders(HttpMethod method, String uriAndParams) {
        String stringToSign =
            method + "\n" + MediaType.APPLICATION_JSON_UTF8 + "\n" + "\n" + MediaType.APPLICATION_JSON_UTF8 + "\n"
                + curTimeStr + "\n" + uriAndParams;

        String sign = getSign(stringToSign);

        HttpHeaders headers = new HttpHeaders();
        headers.setDate(curTime);
        headers.set("x-ca-key", appKey);
        headers.set("x-ca-signature-method", HMAC_SHA256);
        headers.set("x-ca-timestamp", String.valueOf(curTime));
        headers.set("X-Ca-Signature", sign);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        return headers;
    }

}
