package com.solverpeng.cache;

import java.util.Set;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
public interface ICacheClient {
    /**
     * 保存序列化数据
     * @param key
     * @param value
     * @return
     */
    public <T> boolean set(String key,T value);

    /**
     * 保存JSON化数据
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key,String json);

    /**
     * 保存JSON数据
     * @param key
     * @param value
     * @return
     */
    public boolean set_2_0(String key,int TTL, String json);

    /**
     * 保存有有效期的数据
     * @param key
     * @param value
     * @param 数据超时的秒数
     * @return
     */
    public <T> boolean set(String key, int TTL, T value);

    /**
     * 重置超时时间
     * @param key
     * @param value
     * @param 数据超时的秒数
     * @return
     */
    public <T> boolean expire(String key, int TTL);

    /**
     * 获取序列化的缓存数据
     * @param key
     * @return
     */
    public <T> T get(String key);

    /**
     * 获取JSON化的缓存数据
     * @param key
     * @return
     */
    public String get_2_0(String key);

    /**
     * 移出缓存数据
     * @param key
     * @return
     */
    public boolean delete(String key);

    /**
     * 缓存中是否存在key
     * @param key
     * @return
     */
    public boolean exists(String key);

    /**
     * 删除所有缓存内的数据
     * @return
     */
    public boolean clear();

    /**
     * 缓存数据数量
     * @return
     */
    public int size();

    /**
     * 缓存所有的key的集合
     * @return
     */
    public Set<String> keySet(String pattern);

    /**
     * 缓存key的剩余生命周期
     * @return
     */
    public long ttl(String key);
}