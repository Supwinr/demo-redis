package com.yufei.cache;

import java.io.Serializable;

/**
 * Created by pc on 2016-12-16.
 */
public class CacheJson implements Serializable {

    private String key;
    private String json;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
