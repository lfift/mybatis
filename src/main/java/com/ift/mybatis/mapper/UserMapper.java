package com.ift.mybatis.mapper;

import com.ift.mybatis.annotation.DataScope;
import com.ift.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper
 *
 * @author liufei
 */
public interface UserMapper {


    @DataScope("users")
    User selectByPrimaryKey(@Param("id") Long id);

    @DataScope("users")
    User selectById();

    int insert(User user);
}
