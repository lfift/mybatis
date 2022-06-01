package com.ift.mybatis;

import com.ift.mybatis.entity.User;
import com.ift.mybatis.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author liufei
 */
public class MybatisTest {

    private SqlSessionFactory sessionFactory;

    @Before
    public void buildSessionFactory() throws IOException {
        this.sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
    }

    @Test
    public void selectByPrimaryKey() {
        SqlSession sqlSession = sessionFactory.openSession(true);
//        Object o = sqlSession.selectOne("com.ift.mybatis.mapper.UserMapper.selectByPrimaryKey", 1L);
//        System.out.println(o);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectByPrimaryKey(1L);
        System.out.println(user);
    }

    @Test
    public void insert() {
        User user = new User();
        user.setId(1L);
        user.setName("zhangSan");
        user.setAge(18);
        user.setSex((byte) 1);
        System.out.println(sessionFactory.openSession(true).getMapper(UserMapper.class).insert(user));
    }
}
