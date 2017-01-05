package com.solverpeng.redis.test;

import com.solverpeng.cache.RedisPool;
import com.solverpeng.redis.RedisComponent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
public class RedisTest {
    ApplicationContext ctx = null;
    RedisComponent redisComponent = null;
    @Before
    public void init() {
        ctx = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
        redisComponent = ctx.getBean(RedisComponent.class);
    }

    @Test
    public void testGetRedisList() {
        redisComponent.testGetRedisList();
    }

    @Test
    public void testRedisListSave() {
        redisComponent.testSaveRedisList();
    }

    @Test
    public void testRedisGet() {
        redisComponent.testRedisGet();
    }

    @Test
    public void testRedisSave() {
        redisComponent.testRedisSave();
    }

    @Test
    public void testJdbcTemplate() {
        JdbcTemplate bean = ctx.getBean(JdbcTemplate.class);
        System.out.println(bean);
    }

    @Test
    public void testRedisPool() {
        RedisPool bean = ctx.getBean(RedisPool.class);
        System.out.println(bean);
    }


    @Test
    public void testConnection() throws SQLException {
        DataSource dataSource = (DataSource)ctx.getBean("dataSource");
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
