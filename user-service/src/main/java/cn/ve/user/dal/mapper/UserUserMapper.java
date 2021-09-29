package cn.ve.user.dal.mapper;

import cn.ve.user.dal.entity.UserUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表(UserUser)表数据库访问层
 *
 * @author makejava
 * @since 2021-09-20 21:44:14
 */
public interface UserUserMapper {

    UserUser queryById(@Param("id") Long id);

    List<UserUser> queryAll(UserUser userUser);

    int insert(UserUser userUser);

    int update(UserUser userUser);

    int deleteById(@Param("id") Long id);

}

