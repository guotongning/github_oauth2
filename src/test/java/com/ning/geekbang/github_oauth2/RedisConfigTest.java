package com.ning.geekbang.github_oauth2;

import com.ning.geekbang.github_oauth2.lua.LuaScriptActuator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GithubOauth2Application.class)
public class RedisConfigTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LuaScriptActuator luaScriptActuator;

    @Test
    public void testRedis() {
        stringRedisTemplate.opsForValue().set("nicholas", "this is my name", 10);
        String description = stringRedisTemplate.opsForValue().get("nicholas");
        log.info("description = {}", description);
    }

    @Test
    public void executeLuaScript() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            luaScriptActuator.testScript();
        }
        long end = System.currentTimeMillis();
        log.info("cost time = {}ms", end - start);
    }
}
