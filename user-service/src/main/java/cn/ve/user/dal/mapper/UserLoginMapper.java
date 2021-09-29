package cn.ve.user.dal.mapper;

import cn.ve.user.dal.entity.UserLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 登陆表(UserLogin)表数据库访问层
 *
 * @author makejava
 * @since 2021-09-20 13:15:17
 */
public interface UserLoginMapper {

    UserLogin queryById(@Param("id") Long id);

    List<UserLogin> queryAll(UserLogin userLogin);

    int insert(UserLogin userLogin);

    int update(UserLogin userLogin);

    int deleteById(@Param("id") Long id);

}

