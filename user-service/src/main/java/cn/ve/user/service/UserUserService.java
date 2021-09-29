package cn.ve.user.service;

import cn.ve.user.dal.entity.UserUser;
import cn.ve.user.param.UserUserQO;

import java.util.List;

public interface UserUserService {

    UserUser queryById(Long id);

    List<UserUser> queryAll(UserUserQO qo);

    int insert(UserUser userUser);

    int update(UserUser userUser);

    int deleteById(Long id);

    void updateStatus(Long id, Integer status);

}

