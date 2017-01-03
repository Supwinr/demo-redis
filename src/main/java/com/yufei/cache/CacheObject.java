package com.yufei.cache;

import java.io.Serializable;

/**
 * Created by pc on 2016-12-16.
 */
public class CacheObject<T> implements Serializable {

    private String key;
    private T object;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

}
