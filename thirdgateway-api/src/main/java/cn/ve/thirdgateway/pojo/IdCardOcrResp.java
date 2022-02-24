package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2021/9/7
 */

@Data
public class IdCardOcrResp implements Serializable {
    //***************公共字段
    /**
     * 配置信息，同输入configure
     */
    private String config_str;
    /**
     * 是否是复印件
     */
    private String is_fake;
    /**
     * 识别结果，true表示成功，false表示失败
     */
    private String success;

    /*************************下面是正面字段

     /**
     *  姓名
     */
    private String name;
    /**
     * 民族
     */
    private String nationality;
    /**
     * 身份证号
     */
    private String num;
    /**
     * 性别
     */
    private String sex;
    /**
     * 出生日期
     */
    private String birth;

    /**
     * // 地址信息
     */
    private String address;
    /**
     * // 人脸位置
     */
    private Object face_rect;
    /**
     * // 表示人脸矩形长宽
     */
    private String size;
    /**
     * // 身份证区域位置，四个顶点表示，顺序是逆时针(左上、左下、右下、右上)
     */
    private Object card_region;
    /**
     * // 人脸位置，四个顶点表示
     */
    private Object face_rect_vertices;

    /*************************下面是反面字段
     /**
     * 有效期起始时间
     */
    private String start_date;
    /**
     * 有效期结束时间
     */
    private String end_date;
    /**
     * 签发机关
     */
    private String issue;
    /**
     * 文件存储uri
     */
    private String fileUri;
}