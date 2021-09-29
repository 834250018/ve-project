package cn.ve.user.service;

import cn.ve.user.dal.entity.UserLogin;
import cn.ve.user.param.UserLoginQO;

import java.util.List;

public interface UserLoginService {

    UserLogin queryById(Long id);

    List<UserLogin> queryAll(UserLoginQO qo);

    int insert(UserLogin userLogin);

    int update(UserLogin userLogin);

    int deleteById(Long id);

    void updateStatus(Long id, Integer status);

}

