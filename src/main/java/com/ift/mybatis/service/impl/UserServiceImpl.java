package com.ift.mybatis.service.impl;

import com.ift.mybatis.entity.User;
import com.ift.mybatis.mapper.UserMapper;
import com.ift.mybatis.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liufei
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User query(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }
}
