package cn.ve.user.dal.mapper.ext;

import cn.ve.user.dal.entity.UserLogin;
import cn.ve.user.param.UserLoginQO;
import cn.ve.user.dal.mapper.UserLoginMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 登陆表(UserLogin)表数据库访问层
 *
 * @author makejava
 * @since 2021-09-20 13:15:19
 */
public interface UserLoginExtMapper extends UserLoginMapper {

    List<UserLogin> queryList(UserLoginQO qo);

    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

}

