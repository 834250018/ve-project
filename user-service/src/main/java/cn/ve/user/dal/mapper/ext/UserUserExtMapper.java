package cn.ve.user.dal.mapper.ext;

import cn.ve.user.dal.entity.UserUser;
import cn.ve.user.param.UserUserQO;
import cn.ve.user.dal.mapper.UserUserMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 用户表(UserUser)表数据库访问层
 *
 * @author makejava
 * @since 2021-09-20 21:44:15
 */
public interface UserUserExtMapper extends UserUserMapper {

    List<UserUser> queryList(UserUserQO qo);

    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

}

