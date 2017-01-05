package com.solverpeng.cache.impl;

import com.alibaba.fastjson.JSON;
import com.solverpeng.cache.*;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
public class RedisClient implements ICacheClient {

    private JedisPool pool;

    /**
     * Get方法, 转换结果类型并屏蔽异常,仅返回Null.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {

        if (StringUtils.isBlank(key)) {
            return null;
        }

        T result = null;

        Jedis redis = null;

        boolean borrowOrOprSuccess = true;

        try {
            redis = pool.getResource();
            redis.select(0);

            byte[] x = redis.get(key.getBytes());

            if (x == null) {
                return null;
            }

            CacheObject<T> co = (CacheObject<T>) SerializeUtil.unserialize(x);

            if (co != null) {
                return co.getObject();
            }

        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (redis != null)
                pool.returnBrokenResource(redis);

        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(redis);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public boolean exists(String key) {

        boolean result = false;

        if (StringUtils.isBlank(key)) {
            return false;
        }

        Jedis redis = null;

        boolean borrowOrOprSuccess = true;

        try {
            redis = pool.getResource();
            redis.select(0);

            result = redis.exists(key);

        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (redis != null)
                pool.returnBrokenResource(redis);

        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(redis);
        }

        return result;
    }

    /**
     * 获取存储的JSON数据
     */
    @SuppressWarnings("unchecked")
    public String get_2_0(String key) {

        if (StringUtils.isBlank(key)) {
            return null;
        }

        String result = null;

        Jedis redis = null;

        boolean borrowOrOprSuccess = true;

        try {
            redis = pool.getResource();
            redis.select(0);

            String x = redis.get(key);

            if (StringUtils.isBlank(x)) {
                return "";
            }

            CacheJson co = JSON.parseObject(x, CacheJson.class);

            if (co != null) {
                return co.getJson();
            }

        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (redis != null)
                pool.returnBrokenResource(redis);

        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(redis);
        }

        return result;
    }

    public void destroy() throws Exception {
        pool.destroy();
    }

    public <T> boolean set(String key, T value) {
        return set(key, 0, value);
    }

    public <T> boolean set(String key, int TTL, T value) {

        if (value == null || StringUtils.isBlank(key)) {
            return false;
        }

        Jedis redis = null;

        boolean borrowOrOprSuccess = true;

        try {
            redis = pool.getResource();
            redis.select(0);
            CacheObject<T> co = new CacheObject<T>();
            co.setObject(value);
            co.setKey(key);

            if (TTL == 0) {
                redis.set(key.getBytes(), SerializeUtil.serialize(co));
            } else {
                redis.setex(key.getBytes(), TTL, SerializeUtil.serialize(co));
            }


        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (redis != null)
                pool.returnBrokenResource(redis);

            return false;

        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(redis);
        }

        return true;
    }

    /**
     * JSON格式存储
     *
     * @param key
     * @param TTL
     * @param json
     * @return
     */
    public boolean set(String key, String json) {
        return set(key, 0, json);
    }

    /**
     * JSON格式存储
     *
     * @param key
     * @param TTL  秒
     * @param json
     * @return
     */
    public boolean set_2_0(String key, int TTL, String json) {

        if (StringUtils.isBlank(key)) {
            return false;
        }

        Jedis redis = null;

        boolean borrowOrOprSuccess = true;

        try {
            redis = pool.getResource();
            redis.select(0);
            CacheJson cj = new CacheJson();
            cj.setJson(json);
            cj.setKey(key);

            if (TTL == 0) {
                redis.set(key, JSON.toJSONString(cj));
            } else {
                redis.setex(key, TTL, JSON.toJSONString(cj));
            }


        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (redis != null)
                pool.returnBrokenResource(redis);

            return false;

        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(redis);
        }

        return true;
    }

    public boolean delete(String key) {

        if (StringUtils.isBlank(key)) {
            return false;
        }

        Jedis redis = null;

        boolean borrowOrOprSuccess = true;

        try {
            redis = pool.getResource();
            redis.select(0);
            redis.del(key);

        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (redis != null)
                pool.returnBrokenResource(redis);

            return false;

        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(redis);
        }

        return true;

    }

    public boolean clear() {
        return false;
    }

    public int size() {
        return 0;
    }

    public Set<String> keySet(String pattern) {
        Jedis redis = null;
        Set<String> keysSet = new HashSet<String>();

        boolean borrowOrOprSuccess = true;

        try {
            redis = pool.getResource();
            redis.select(0);
            keysSet = redis.keys(pattern);

        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (redis != null)
                pool.returnBrokenResource(redis);
        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(redis);
        }

        return keysSet;
    }


    public <T> boolean expire(String key, int TTL) {

        if (StringUtils.isBlank(key)) {
            return false;
        }

        Jedis redis = null;

        boolean borrowOrOprSuccess = true;

        try {
            redis = pool.getResource();
            redis.select(0);
            redis.expire(key, TTL);

        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (redis != null)
                pool.returnBrokenResource(redis);
        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(redis);
        }

        return true;
    }

    public List<CacheObject> getCacheObjectList(List<String> keyList) {

        if (keyList != null && keyList.size() > 0) {

            List<CacheObject> cacheList = new ArrayList<CacheObject>();

            Jedis redis = null;

            boolean borrowOrOprSuccess = true;

            try {
                redis = pool.getResource();
                redis.select(0);

                for (String key : keyList) {
                    CacheObject co = new CacheObject();
                    co.setKey(key);
                    if (!key.startsWith("TGT") && !key.startsWith("SESSION")) {
                        byte[] x = redis.get(key.getBytes());
                        if (x != null) {
                            co = (CacheObject) SerializeUtil.unserialize(x);
                        }

                        if (co == null) {
                            co = new CacheObject();
                            co.setKey(key);
                        }
                    }
                    cacheList.add(co);
                }

            } catch (Exception e) {
                borrowOrOprSuccess = false;
                if (redis != null)
                    pool.returnBrokenResource(redis);
            } finally {
                if (borrowOrOprSuccess)
                    pool.returnResource(redis);
            }

            return cacheList;

        }

        return null;
    }

    public void setPool(RedisPool pool) {
        this.pool = pool.getPool();
    }

    public void setJedisPool(JedisPool pool) {
        this.pool = pool;
    }


    public long ttl(String key) {
        if (StringUtils.isBlank(key)) {
            return 0;
        }

        Jedis redis = null;

        boolean borrowOrOprSuccess = true;

        try {
            redis = pool.getResource();
            redis.select(0);
            return redis.ttl(key.getBytes());

        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (redis != null)
                pool.returnBrokenResource(redis);

            return 0;

        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(redis);
        }
    }

}
