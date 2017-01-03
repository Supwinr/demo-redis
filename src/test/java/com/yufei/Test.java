package com.yufei;

import com.yufei.cache.RedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by pc on 2016-12-16.
 */
public class Test {

    public static void main(String[] args) {

        RedisClient client = new RedisClient();
        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数, 默认8个
        config.setMaxTotal(100);
        //最大空闲连接数, 默认8个
        config.setMaxIdle(50);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,默认-1
        config.setMaxWaitMillis(1000);
        //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(true);

        JedisPool pool = new JedisPool(config, "127.0.0.1");
        client.setJedisPool(pool);
        client.set("test", "haha");
        String s = client.get("test");
        System.out.println("普通对象");
        System.out.println(s);
        client.setJson("testt", "{\"smile\":\"heihei\"}");
        String ss = client.getJson("testt");
        System.out.println("json");
        System.out.println(ss);

        // 原生态
        Jedis j = new Jedis("127.0.0.1");
        j.set("t", "tt");
        System.out.println("原生态");
        System.out.println(j.get("t"));
        System.out.println(j.get("a"));

    }

}
