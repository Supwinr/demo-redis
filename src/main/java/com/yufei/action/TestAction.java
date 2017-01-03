package com.yufei.action;

import com.yufei.cache.RedisClient;
import com.yufei.utils.ContextHolderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by pc on 2016-12-13.
 */
@Controller
@RequestMapping(value = "/test")
public class TestAction {

    private static final Logger logger = LoggerFactory.getLogger(TestAction.class);

    @RequestMapping(value = "/setInfo")
    public String setInfo(String info) {
        RedisClient redisClient = ContextHolderUtil.getCacheClient();
        redisClient.set("test", 1000 * 60 * 60 * 24, info);
        return "/setInfo";
    }

    @RequestMapping(value = "/getInfo")
    public String getInfo(Model model) {
        RedisClient redisClient = ContextHolderUtil.getCacheClient();
        model.addAttribute("test", redisClient.get("test"));
        return "/getInfo";
    }

}
