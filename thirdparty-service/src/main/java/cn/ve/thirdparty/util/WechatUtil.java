package cn.ve.thirdparty.util;

import cn.hutool.core.codec.Base64Decoder;
import cn.ve.base.pojo.VeException;
import cn.ve.thirdparty.pojo.WechatUserDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author ve
 * @date 2021/8/10
 */
@Slf4j
public enum WechatUtil {
    ;

    /**
     * 从jscode登录的加密信息里面提取出微信手机号
     *
     * @param data      加/解密数据
     * @param secretKey 密钥
     * @param ivString  向量
     * @return
     * @throws Exception
     */
    public static WechatUserDTO decrypt(String data, String secretKey, String ivString) {
        try {
            byte[] bytes = decrypt(Base64Decoder.decode(data), Base64Decoder.decode(secretKey.getBytes()),
                Base64Decoder.decode(ivString));
            return JSON.parseObject(new String(bytes), WechatUserDTO.class);
        } catch (Exception e) {
            log.error("微信解密异常:{}", e.getMessage(), e);
            throw new VeException("服务器异常");
        }
    }

    /**
     * 微信解密
     *
     * @param data      加/解密数据
     * @param secretKey 密钥
     * @param ivBytes   初始化向量
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] secretKey, byte[] ivBytes) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
        return cipher.doFinal(data);
    }

}
