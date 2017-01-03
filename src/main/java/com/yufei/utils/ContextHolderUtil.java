package com.yufei.utils;

import cn.org.rapid_framework.util.holder.ApplicationContextHolder;
import com.yufei.cache.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

/**
 * Created by pc on 2016-12-16.
 */
public abstract class ContextHolderUtil {

    private static final Logger logger = LoggerFactory.getLogger(ContextHolderUtil.class);

    public static RedisClient getCacheClient() {
        return (RedisClient) getBeanByName("redisClient");
    }

    private static Object getBeanByName(String name) {
        Object bean = null;
        try {
            bean = ApplicationContextHolder.getBean(name);
        } catch (BeansException e) {
            logger.error("some errors occurred", e);
        }
        return bean;
    }

}
