package cn.ve.base.pojo;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class RequestHeader implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private String requestId;
    private Long loginId;
    private Long userId;
    private String nickname;
    private String headPortrait;

    private String language = "zh";
    private long requestTime = System.currentTimeMillis();

    //    private Integer loginType;
    //    private String clientId;
    //    private String timeZone;

}