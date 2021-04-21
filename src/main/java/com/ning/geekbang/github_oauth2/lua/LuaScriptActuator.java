package com.ning.geekbang.github_oauth2.lua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LuaScriptActuator {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public LuaScriptActuator(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private DefaultRedisScript<List> testScript;

    @PostConstruct
    public void init() {
        testScript = new DefaultRedisScript<>();
        testScript.setResultType(List.class);
        testScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/redis_test.lua")));
    }

    public List testScript() {
        List<String> keyList = new ArrayList<>();
        keyList.add("nicholas");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("expire", 60);
        paramMap.put("value", "this is my name, created by lua");
        return redisTemplate.execute(testScript, keyList, paramMap);
    }
}
