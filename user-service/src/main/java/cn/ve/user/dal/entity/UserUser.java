package cn.ve.user.dal.entity;

import cn.ve.base.pojo.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 用户表(UserUser)实体类
 *
 * @author ve
 * @since 2022-02-22 14:15:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserUser extends BaseEntity {
    private static final long serialVersionUID = -32030853332822496L;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 头像
     */
    private String headPortrait;

    /**
     * 性别:0.男；1.女；2.未知；
     */
    private Integer gender;

    /**
     * 现住地省编码
     */
    private String provinceCode;

    /**
     * 现住地市编码
     */
    private String cityCode;

    /**
     * 现住地区编码
     */
    private String areaCode;

    /**
     * 现住地省名称
     */
    private String provinceName;

    /**
     * 现住地市名称
     */
    private String cityName;

    /**
     * 现住地区名称
     */
    private String areaName;

    /**
     * 现住地详细地址
     */
    private String address;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 民族
     */
    private String nation;

    /**
     * 家乡
     */
    private String hometown;

    /**
     * 年龄
     */
    private Integer age;

}
