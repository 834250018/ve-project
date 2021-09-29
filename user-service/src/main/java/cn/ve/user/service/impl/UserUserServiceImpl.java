package cn.ve.user.service.impl;

import cn.ve.user.dal.entity.UserUser;
import cn.ve.user.param.UserUserQO;
import cn.ve.user.dal.mapper.ext.UserUserExtMapper;
import cn.ve.user.service.UserUserService;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;

@Service
public class UserUserServiceImpl implements UserUserService {

    @Resource
    private UserUserExtMapper userUserExtMapper;

    @Override
    public UserUser queryById(Long id) {
        return userUserExtMapper.queryById(id);
    }

    @Override
    public List<UserUser> queryAll(UserUserQO qo) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        return userUserExtMapper.queryList(qo);
    }

    @Override
    public int insert(UserUser userUser) {
        return userUserExtMapper.insert(userUser);
    }

    @Override
    public int update(UserUser userUser) {
        return userUserExtMapper.update(userUser);
    }

    @Override
    public int deleteById(Long id) {
        return userUserExtMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        userUserExtMapper.updateStatus(id, status);
    }

}

