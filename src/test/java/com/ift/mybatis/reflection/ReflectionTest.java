package com.ift.mybatis.reflection;

import com.ift.mybatis.entity.User;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.junit.Test;

/**
 * @author liufei
 */
public class ReflectionTest {

    @Test
    public void test() {
        ObjectFactory objectFactory = new DefaultObjectFactory();
        User user = objectFactory.create(User.class);
        System.out.println(user);
        ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
        ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
        MetaObject metaObject = MetaObject.forObject(user, objectFactory, objectWrapperFactory, reflectorFactory);
        metaObject.setValue("id", "23");
        ObjectWrapper objectWrapper = metaObject.getObjectWrapper();
        objectWrapper.set(new PropertyTokenizer("name"), "ZhangSan");
        System.out.println(user);
    }

}
