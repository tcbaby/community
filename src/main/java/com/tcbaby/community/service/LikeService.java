package com.tcbaby.community.service;

import com.tcbaby.community.pojo.HostHolder;
import com.tcbaby.community.pojo.User;
import com.tcbaby.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author tcbaby
 * @date 20/05/14 9:48
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /** 统计 实体的点赞数 */
    public long countEntityLike(int entityType, int entityId) {
        String key = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Long size = redisTemplate.opsForSet().size(key);
        return size == null ? 0 : size;
    }

    /** 统计 用户获得的点赞数 */
    public int countUserLike(int userId) {
        String key = RedisKeyUtil.getUserLikeKey(userId);

        Integer value = (Integer) redisTemplate.opsForValue().get(key);
        return value == null ? 0 : value;
    }

    /** 查看用户对实体的点赞状态， 没有登录则直接返回0 */
    public int queryEntityLikeStatus(int entityType, int entityId) {
        User user = HostHolder.getUser();
        if (user == null) {
            return 0;
        }
        String key = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Boolean member = redisTemplate.opsForSet().isMember(key, String.valueOf(user.getId()));
        return member ? 1 : 0;
    }

    /** 点赞 或者 取消点赞 */
    public int likeOrUnlike(int entityUserId, int entityType, int entityId) {
        Integer res = (Integer) redisTemplate.execute(new SessionCallback() {
            @Override
            public Integer execute(RedisOperations redisOperations) throws DataAccessException {
                int loginuserId = HostHolder.getUser().getId();
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                Boolean member = redisOperations.opsForSet().isMember(entityLikeKey, loginuserId);

                redisOperations.multi();
                if (member) {
                    // 取消点赞
                    redisOperations.opsForSet().remove(entityLikeKey, loginuserId);
                    redisOperations.opsForValue().decrement(userLikeKey);
                } else {
                    // 点赞
                    redisOperations.opsForSet().add(entityLikeKey, loginuserId);
                    redisOperations.opsForValue().increment(userLikeKey);
                }
                redisOperations.exec();

                return member ? 0 : 1;
            }
        });
        return res;
    }
}
