package com.ift.mybatis.mapper;

import com.ift.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper
 *
 * @author liufei
 */
public interface UserMapper {


    User selectByPrimaryKey(@Param("id") Long id);

    int insert(User user);
}
