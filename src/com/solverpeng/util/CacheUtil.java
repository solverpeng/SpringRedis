package com.solverpeng.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.solverpeng.bean.User;
import com.solverpeng.cache.CacheConstant;
import com.solverpeng.cache.ICacheClient;
import com.solverpeng.cache.impl.RedisClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
public class CacheUtil {
    private static ICacheClient client = null;
    private static JdbcTemplate jdbcTemplate = null;

    static {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
        client = ctx.getBean(RedisClient.class);
        jdbcTemplate = ctx.getBean(JdbcTemplate.class);
    }

    private static final String T_USER_SQL = "select id ,user_name as userName from t_user ";

    //默认3天
    public static final int DEFAULT_EXPIRED_TIME = 3 * 24 * 60 * 60;

    /**
     * 新增缓存数据
     */
    public static void save4Object(String prefixKey, String key, Object o) {
        String jsonString = JSON.toJSONString(o);
        String cacheKey = prefixKey + "_JSON:" + key;
        client.set_2_0(cacheKey, DEFAULT_EXPIRED_TIME, jsonString);
    }

    /**
     * 删除指定单条记录的缓存数据
     *
     * @param prefixKey
     * @param key
     */
    public static void delete4Object(String prefixKey, String key) {
        String cacheKey = prefixKey + "_JSON:" + key;
        client.delete(cacheKey);
    }

    /**
     * 更新指定单条记录的缓存数据
     *
     * @param prefixKey
     * @param key
     */
    public static void update4Object(String prefixKey, String key, Object o) {
        String cacheKey = prefixKey + "_JSON:" + key;
        String jsonString = JSON.toJSONString(o);
        client.set_2_0(cacheKey, DEFAULT_EXPIRED_TIME, jsonString);
    }

    /**
     * 通过KEY从缓存中获取Object数据，若在缓存中不存在，则从数据库中读取,并放入缓存。
     */
    public static <T> T get4Object(Class<T> clazz, String prefixKey, String key) {
        String cacheKey = prefixKey + "_JSON:" + key;
        String value = client.get_2_0(cacheKey);
        T t = null;
        if (StringUtils.isBlank(value)) {
            try {
                value = get4ObjectJson_2_0(prefixKey, key);
                if (StringUtils.isNotBlank(value)) {
                    client.set_2_0(cacheKey, DEFAULT_EXPIRED_TIME, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isNotBlank(value)) {
            t = JSONObject.parseObject(value, clazz);
        }

        return t;
    }

    public static String get4ObjectJson_2_0(String prefixKey, String key) {
        String result = "";
        List<Map<String, Object>> sqlList = jdbcTemplate.queryForList("SELECT SQL_TEXT FROM t_cache_config WHERE STORE_TYPE=? AND  CACHE_KEY=?", "Object", prefixKey);
        List<Map<String, Object>> queryForList = null;
        if (sqlList != null) {
            String sql = (String) sqlList.get(0).get("SQL_TEXT");
            if (StringUtils.isNotBlank(sql)) {
                queryForList = jdbcTemplate.queryForList(sql, key);
                if (queryForList != null && queryForList.size() == 1) {
                    result = JSON.toJSONString(queryForList.get(0));
                }
            }
        }

        return result;
    }

    /**
     * 通过KEY从缓存中获取List数据，若在缓存中不存在，则从数据库中读取，并放入缓存
     *
     * @param clazz
     * @param key
     */

    public static <T> List<T> get4List_2_0(Class<T> clazz, String key) {
        String result = client.get_2_0(key);
        List<T> cacheList = null;
        if (StringUtils.isNotBlank(result)) {
            cacheList = JSON.parseArray(result, clazz);
        }

        if (cacheList == null) {

            if (clazz == String.class) {
                cacheList = (List<T>) get4ListString(key);
            } else {
                cacheList = getListObjectFromDb(clazz, key, null);
            }

            if (cacheList != null && cacheList.size() > 0) {
                client.set_2_0(key, DEFAULT_EXPIRED_TIME, JSON.toJSONString(cacheList));
            }
        }

        return cacheList == null ? new ArrayList<T>() : cacheList;
    }

    private static List<String> get4ListString(String key) {

        List<Map<String, Object>> sqlList = jdbcTemplate.queryForList("SELECT SQL_TEXT FROM t_cache_config WHERE STORE_TYPE=? AND CACHE_KEY=?", "List", key);
        if (sqlList.size() != 0) {
            String sqlText = (String) sqlList.get(0).get("SQL_TEXT");
            if (StringUtils.isNotBlank(sqlText)) {

                RowMapper<String> rowMapper = null;
                List<String> cacheList = jdbcTemplate.query(sqlText, rowMapper);
                if (cacheList != null) {
                    return cacheList;
                }

            }
        }

        return new ArrayList<String>();
    }

    private static <T> List<T> getListObjectFromDb(Class<T> clazz, String prefixKey, String key) {
        List<T> cacheList = new ArrayList<T>();
        try {
            String result = get4ListObjectJson(prefixKey, key);
            if (StringUtils.isNotBlank(result)) {
                cacheList = JSON.parseArray(result, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheList;
    }

    private static String get4ListObjectJson(String prefixKey, String key) {
        List<Map<String, Object>> sqlList = jdbcTemplate.queryForList("SELECT SQL_TEXT FROM t_cache_config WHERE STORE_TYPE=? AND CACHE_KEY=?", "List", prefixKey);
        if (sqlList.size() != 0) {
            String sqlText = (String) sqlList.get(0).get("SQL_TEXT");
            if (StringUtils.isNotBlank(sqlText)) {
                List<Map<String, Object>> cacheList = jdbcTemplate.queryForList(sqlText, key);
                return JSON.toJSONString(cacheList);
            }
        }
        return "";
    }

    /**
     * 从数据库读取同步方法
     *
     * @param key
     * @return
     */
    public static void setRedisFromDB(String key) {
        List<String> values = getDBDate(key);
        if (CollectionUtils.isNotEmpty(values)) {
            if (CacheConstant.T_USER.equals(key)) {
                for (String value : values) {
                    User user = JSON.parseObject(value, User.class);
                    client.set_2_0(key + "_JSON:" + user.getId(), DEFAULT_EXPIRED_TIME, value);
                }
            }
        }
    }

    private static List<String> getDBDate(String key) {
        List<String> result = new ArrayList<String>();
        String sqlText = null;
        if (CacheConstant.T_USER.equals(key)) {
            sqlText = T_USER_SQL;
        }
        if (StringUtils.isNotBlank(sqlText)) {
            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sqlText);
            for (int i = 0; i < mapList.size(); i++) {
                result.add(JSON.toJSONString(mapList.get(i)));
            }
        }
        return result;
    }

    /**
     * 从数据库读取指定KEY对应的List数据
     *
     * @param clazz     要返回的数据类型,For Example : TsysUser.class
     * @param prefixKey 一类缓存的标示，一般配置在CacheConstant类下
     * @param key       和prefixKey参数组装成key，在缓存中查询
     * @return
     */
    public static <T> List<T> get4List_2_0(Class<T> clazz, String prefixKey, String key) {
        String cacheKey = prefixKey + "_JSON:" + key;
        String result = client.get_2_0(cacheKey);
        List<T> cacheList = null;
        if (StringUtils.isNotBlank(result)) {
            cacheList = JSON.parseArray(result, clazz);
        }

        if (cacheList == null) {
            cacheList = getListObjectFromDb(clazz, prefixKey, key);
            if (cacheList != null && cacheList.size() > 0) {
                client.set_2_0(cacheKey, DEFAULT_EXPIRED_TIME, JSON.toJSONString(cacheList));
            }
        }

        return cacheList == null ? new ArrayList<T>() : cacheList;
    }
}
