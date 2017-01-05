package com.solverpeng.cache;

import com.solverpeng.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
public class RedisPool  implements InitializingBean, DisposableBean {
    private int maxActive = 400;
    private int maxIdle = 20;
    private int maxWait = 10000;

    private JedisPool pool;
    private String redisNodes;

    public void destroy() throws Exception {
        pool.destroy();
    }

    public void afterPropertiesSet() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWait(maxWait);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        try {
            if(StringUtils.isNotBlank(redisNodes) && redisNodes.contains(":")){
                String[] redisAddr = redisNodes.split(":");
                if(Utils.isNumber(redisAddr[1])){
                    pool = new JedisPool(config, redisAddr[0],Integer.parseInt(redisAddr[1]));
                }
            }else {
                pool = new JedisPool(config, redisNodes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public JedisPool getPool() {
        return pool;
    }

    public void setRedisNodes(String redisNodes) {
        this.redisNodes = redisNodes;
    }
}
