package com.skunk.data.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.skunk.data.SqlMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SqlSessionFactoryConfiguration {

    @Autowired
    DataSource dataSource;

    @Bean
    @Primary
    public SqlSessionFactory sqlSession() {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        try {
            sqlSessionFactoryBean.setDataSource(dataSource);
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlMapper getSqlMapper() {
        return new SqlMapper(sqlSession().openSession(true));
    }
}