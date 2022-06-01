package com.ift.mybatis.spring;

import com.ift.mybatis.entity.User;
import com.ift.mybatis.service.IUserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author liufei
 */
public class MybatisSpringTest {

    @Autowired
    private IUserService userService;
    private ApplicationContext applicationContext;
    @Before
    public void before() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    @Test
    public void select() {
        User user = applicationContext.getBean(IUserService.class).query(1L);
        System.out.println(user);
    }
}
