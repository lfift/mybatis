package com.ift.mybatis.config;

import com.ift.mybatis.mapper.UserMapper;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author liufei
 */
@Configuration
@PropertySource("classpath:db.properties")
public class MybatisConfiguration {

    @Value("${driver}")
    private String driver;
    @Value("${url}")
    private String url;
    @Value("${db_username}")
    private String username;
    @Value("${password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        return new UnpooledDataSource(driver, url, username, password);
    }

    @Bean(value = "sqlSessionFactoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml");
        factoryBean.setMapperLocations(resources);
        factoryBean.setDataSource(dataSource());
        return factoryBean;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        scannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        scannerConfigurer.setBasePackage("com.ift.mybatis.mapper");
        return scannerConfigurer;
    }

    @Bean
    public MapperFactoryBean<UserMapper> userMapper(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<UserMapper> userMapper = new MapperFactoryBean<>(UserMapper.class);
        userMapper.setSqlSessionFactory(sqlSessionFactory);
        return userMapper;
    }
}
