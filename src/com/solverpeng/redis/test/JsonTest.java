package com.solverpeng.redis.test;

import com.alibaba.fastjson.JSON;
import com.solverpeng.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5 0005.
 */
public class JsonTest {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setUserName("aa");
        User user2 = new User();
        user2.setId(2);
        user2.setUserName("bb");
        users.add(user);
        users.add(user2);

        String jsonString = JSON.toJSONString(users);
        System.out.println(jsonString);
    }
}
