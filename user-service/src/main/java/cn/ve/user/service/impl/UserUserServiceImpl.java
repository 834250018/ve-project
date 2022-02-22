package cn.ve.user.service.impl;

import cn.ve.user.dal.entity.UserUser;
import cn.ve.user.dal.mapper.UserUserMapper;
import cn.ve.user.param.UserUserQO;
import cn.ve.user.service.UserUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserUserServiceImpl implements UserUserService {

    @Resource
    private UserUserMapper userUserMapper;

    @Override
    public UserUser queryById(Long id) {
        return userUserMapper.selectById(id);
    }

    @Override
    public List<UserUser> queryAll(UserUserQO qo) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        return userUserMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public int insert(UserUser userUser) {
        return userUserMapper.insert(userUser);
    }

    @Override
    public int update(UserUser userUser) {
        return userUserMapper.updateById(userUser);
    }

    @Override
    public int deleteById(Long id) {
        return userUserMapper.deleteById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
//        userUserMapper.updateStatus(id, status);
    }

}

