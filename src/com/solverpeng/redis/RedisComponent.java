package com.solverpeng.redis;

import com.solverpeng.bean.User;
import com.solverpeng.cache.CacheConstant;
import com.solverpeng.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
@Component
public class RedisComponent {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void testGetRedisList() {
        List<User> users = CacheUtil.get4List_2_0(User.class, CacheConstant.T_USER_LIST, "list");
        System.out.println(users);
    }

    public void testSaveRedisList() {
        String sql = "select id, user_name from t_user";
        RowMapper<User> mapper = new BeanPropertyRowMapper(User.class);
        List<User> users = jdbcTemplate.query(sql, mapper);
        CacheUtil.save4Object(CacheConstant.T_USER_LIST, "list", users);
    }

    public void testRedisSave() {
        String sql = "select id, user_name from t_user where id = ?";
        RowMapper<User> mapper = new BeanPropertyRowMapper(User.class);
        User user = jdbcTemplate.queryForObject(sql, mapper, 1);
        CacheUtil.save4Object(CacheConstant.T_USER, user.getId().toString(), user);
    }

    public void testRedisGet() {
        String userId = "2";
        User user = CacheUtil.get4Object(User.class, CacheConstant.T_USER, userId);
        System.out.println(user);
    }

}
