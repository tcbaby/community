package com.tcbaby.community.test;

import com.tcbaby.community.mapper.UserMapper;
import com.tcbaby.community.service.FollowService;
import com.tcbaby.community.service.UserService;
import com.tcbaby.community.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tcbaby
 * @date 20/05/14 11:57
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FollowService followService;

    @Test
    public void test() {
        String key = "temp";
        redisTemplate.opsForValue().set(key, null);
    }

    // 降智打击

    @Test
    public void createFollowing() {
        int[] ids = {132,120,121,122,123,124,125,126,127,128,129,119,133,134,137,138,144,13,21,22,23,24,25,101,1,103,111,112,113,114,115};
        int ownerId = 167;

        for (int id : ids) {
            redisTemplate.execute(new SessionCallback() {
                @Override
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    String followerKey = RedisKeyUtil.getFollowerKey(3, id);
                    String followingKey = RedisKeyUtil.getFollowingKey(ownerId, 3);

                    redisOperations.opsForZSet().add(followerKey, ownerId, System.currentTimeMillis());
                    redisOperations.opsForZSet().add(followingKey, id, System.currentTimeMillis());
                    return null;
                }
            });
        }
    }

    @Test
    public void createFollowers() {
        int[] ids = {132,120,121,122,123,124,125,13,21,22,23,24,25,101,1,103,111,112,113,114,115};
        int ownerId = 167;

        for (int id : ids) {
            redisTemplate.execute(new SessionCallback() {
                @Override
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    String followerKey = RedisKeyUtil.getFollowerKey(3, ownerId);
                    String followingKey = RedisKeyUtil.getFollowingKey(id, 3);

                    redisOperations.opsForZSet().add(followerKey, id, System.currentTimeMillis());
                    redisOperations.opsForZSet().add(followingKey, ownerId, System.currentTimeMillis());
                    return null;
                }
            });
        }
    }
}
