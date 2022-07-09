package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.User;
import com.hechi.niumall.mapper.UserMapper;
import com.hechi.niumall.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUser() {
        return getById(1);
    }
}
