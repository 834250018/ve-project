package cn.ve.user.service.impl;

import cn.ve.user.dal.entity.UserLogin;
import cn.ve.user.param.UserLoginQO;
import cn.ve.user.dal.mapper.ext.UserLoginExtMapper;
import cn.ve.user.service.UserLoginService;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Resource
    private UserLoginExtMapper userLoginExtMapper;

    @Override
    public UserLogin queryById(Long id) {
        return userLoginExtMapper.queryById(id);
    }

    @Override
    public List<UserLogin> queryAll(UserLoginQO qo) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        return userLoginExtMapper.queryList(qo);
    }

    @Override
    public int insert(UserLogin userLogin) {
        return userLoginExtMapper.insert(userLogin);
    }

    @Override
    public int update(UserLogin userLogin) {
        return userLoginExtMapper.update(userLogin);
    }

    @Override
    public int deleteById(Long id) {
        return userLoginExtMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        userLoginExtMapper.updateStatus(id, status);
    }

}

