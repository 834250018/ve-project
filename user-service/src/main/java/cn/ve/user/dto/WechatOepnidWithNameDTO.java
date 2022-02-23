package cn.ve.user.dto;

import cn.ve.thirdparty.pojo.WechatOpenidDTO;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * @author <a href="mailto:singofu@163.com">singofu</a>
 * @description <br>
 * @date 2019年7月3日
 * @Copyright (c) 2019, singofu.com
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
