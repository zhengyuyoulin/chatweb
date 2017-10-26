package com.netty.service;

import com.netty.entity.User;
import org.springframework.stereotype.Service;

/**
 * Created by cai on 2017/10/16.
 */
public interface UserService {

    User selectUser(User user);

    User selectUserById(int i);
}
