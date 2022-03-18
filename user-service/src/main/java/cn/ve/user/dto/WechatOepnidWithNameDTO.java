package cn.ve.user.dto;

import cn.ve.thirdgateway.pojo.WechatOpenidDTO;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * 微信响应体
 *
 * @author ve
 * @since 2022-02-23 14:15:26
 */
@Data
@FieldNameConstants
public class WechatOepnidWithNameDTO extends WechatOpenidDTO {

    /**
     * 微信用户头像
     */
    private String headPortrait;

    /**
     * 微信用户昵称
     */
    private String realName;

}
