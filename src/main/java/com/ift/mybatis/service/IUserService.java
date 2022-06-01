package com.ift.mybatis.service;

import com.ift.mybatis.entity.User;

/**
 * @author liufei
 */
public interface IUserService {


    User query(Long userId);
}
