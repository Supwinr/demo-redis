package com.yufei.cache;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by pc on 2016-12-16.
 */
public class RedisPool implements InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(RedisPool.class);

    private int maxTotal = 100;
    private int maxIdle = 20;
    private int maxWaitMillis = 10000;

    private JedisPool pool;
    private String redisNodes;

    public JedisPool getPool() {
        return pool;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public void setRedisNodes(String redisNodes) {
        this.redisNodes = redisNodes;
    }

    public void destroy() throws Exception {
        pool.destroy();
    }

    public void afterPropertiesSet() throws Exception {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        try {
            if (StringUtils.isNotBlank(redisNodes) && redisNodes.contains(":")) {
                String[] redisAddr = redisNodes.split(":");
                if (this.isNumber(redisAddr[1])) {
                    pool = new JedisPool(config, redisAddr[0], Integer.parseInt(redisAddr[1]));
                }
            } else {
                pool = new JedisPool(config, redisNodes);
            }
        } catch (Exception e) {
            logger.error("some errors occurred", e);
        }
    }

    private boolean isNumber(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.matches("[\\d.]+");
        }
        return false;
    }
}
