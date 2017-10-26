package com.netty.service.imp;

import java.util.List;

import javax.annotation.Resource;

import com.netty.dao.UserMapper;
import com.netty.entity.User;
import com.netty.entity.UserExample;
import com.netty.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by ren on 2017/10/16.
 */
@Service
public class UserServiceImp implements UserService {

    @Resource
    private UserMapper userMapper;

    public User selectUser(User user) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
        List<User> list = userMapper.selectByExample(userExample);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public User selectUserById(int i) {
        return userMapper.selectByPrimaryKey(i);
    }
}
