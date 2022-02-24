package cn.ve.thirdgateway.util;

import cn.ve.base.pojo.VeException;
import cn.ve.base.util.ImgUtil;
import cn.ve.thirdgateway.pojo.BaiduAuthResp;
import cn.ve.thirdgateway.pojo.BaiduFaceMatchResp;
import cn.ve.thirdgateway.pojo.BaiduReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 现在临时用我个人的账号,2qps,只能做测试使用
 *
 * @author ve
 * @date 2021/9/1
 */
@Slf4j
public class BaiduFaceUtils {

    private static final String BAIDU_FACE_MATCH_URL = "https://aip.baidubce.com/rest/2.0/face/v3/match?access_token=";
    private static final String BAIDU_AUTH_HOST = "https://aip.baidubce.com/oauth/2.0/token?";
    private final String ACCESS_TOKEN;

    public BaiduFaceUtils(String ak, String sk) {
        // 获取token地址
        String getAccessTokenUrl = BAIDU_AUTH_HOST
            // 1. grant_type为固定参数
            + "grant_type=client_credentials"
            // 2. 官网获取的 API Key
            + "&client_id=" + ak
            // 3. 官网获取的 Secret Key
            + "&client_secret=" + sk;
        try {
            ResponseEntity<BaiduAuthResp> resp =
                new RestTemplate().getForEntity(getAccessTokenUrl, BaiduAuthResp.class);
            this.ACCESS_TOKEN = resp.getBody().getAccess_token(); // todo 有效期为30天
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new VeException("服务器异常");
        }
    }

    public boolean faceMatch(String face1, String face2, String imgType) {
        BaiduFaceMatchResp baiduResp = faceMatch0(face1, face2, imgType);
        if (baiduResp == null || baiduResp.getError_code() == null || baiduResp.getError_code() != 0) {
            System.out.println("人脸比对调用异常");
        }
        // 阈值 80分
        return baiduResp.getResult().getScore() >= 80;
    }

    public BaiduFaceMatchResp faceMatch0(String face1, String face2, String imgType) {
        // 请求url
        try {
            List<BaiduReq> param = new ArrayList<>();
            param.add(new BaiduReq());
            param.add(new BaiduReq());
            param.get(0).setImage(ImgUtil.imgBase64(face1));
            param.get(0).setImage_type(imgType);
            param.get(1).setImage(ImgUtil.imgBase64(face2));
            param.get(1).setImage_type(imgType);

            ResponseEntity<BaiduFaceMatchResp> exchange = new RestTemplate()
                .exchange(BAIDU_FACE_MATCH_URL + ACCESS_TOKEN, HttpMethod.POST, new HttpEntity<>(param),
                    BaiduFaceMatchResp.class);
            return exchange.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}



