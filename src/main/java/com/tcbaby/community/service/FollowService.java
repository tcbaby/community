package com.tcbaby.community.service;

import com.tcbaby.community.pojo.HostHolder;
import com.tcbaby.community.pojo.User;
import com.tcbaby.community.util.CommunityConstant;
import com.tcbaby.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author tcbaby
 * @date 20/05/14 15:35
 */
@Service
public class FollowService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    /** 统计 某个实体的粉丝数 */
    public long countFollowers(int entityType, int entityId) {
        String key = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 统计 用户 对某类实体 的关注数
     * @param userId
     * @param entityType： 1-关注的帖子数， 2-关注的评论数， 3-关注的用户数
     * @return
     */
    public long countFollowing(int userId, int entityType) {
        String key = RedisKeyUtil.getFollowingKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(key);
    }

    /** 用户对某个实体的关注状态 */
    public int queryFollowStatus(int entityType, int entityId) {
        User loginuser = HostHolder.getUser();
        if (loginuser == null) {
            return 0;
        }
        String key = RedisKeyUtil.getFollowerKey(entityType, entityId);
        Double score = redisTemplate.opsForZSet().score(key, loginuser.getId());
        return score == null ? 0 : 1;
    }

    /** 关注、取消关注 */
    public int followOrUnfollow(int entityType, int entityId) {
        User loginuser = HostHolder.getUser();
        // 当实体类型为用户时， 排除自己  不允许自己关注自己
        if (CommunityConstant.ENTITY_TYPE_USER == entityType && entityId == loginuser.getId()) {
            return 0;
        }
        // 获取原来的关注状态
        int oldFollowStatus = queryFollowStatus(entityType, entityId);

        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String followingKey = RedisKeyUtil.getFollowingKey(loginuser.getId(), entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                redisOperations.multi();
                if (oldFollowStatus == 1) {
                    // 取消关注
                    redisOperations.opsForZSet().remove(followingKey, entityId);
                    redisOperations.opsForZSet().remove(followerKey, loginuser.getId());
                } else {
                    // 关注
                    redisOperations.opsForZSet().add(followingKey, entityId, System.currentTimeMillis());
                    redisOperations.opsForZSet().add(followerKey, loginuser.getId(), System.currentTimeMillis());
                }
                redisOperations.exec();
                return null;
            }
        });
        return oldFollowStatus == 1 ? 0 : 1;
    }

    /** 查看实体的粉丝列表 */
    public List<Map<String, Object>> queryFollowers(int entityType, int entityId, int page, int size) {
        int offset = (page - 1) * size;

        String key = RedisKeyUtil.getFollowerKey(entityType, entityId);
        BoundZSetOperations ops = redisTemplate.boundZSetOps(key);
        // 逆序 先获取最新最近关注的
        Set<Integer> ids = ops.reverseRange(offset, offset + size - 1);

        List<Map<String, Object>> followers = new ArrayList<>();
        for (Integer id : ids) {
            Map<String, Object> follower = new HashMap<>();
            follower.put("user", userService.queryUserById(id));
            follower.put("followTime", new Date(ops.score(id).longValue()));
            follower.put("followStatus", queryFollowStatus(CommunityConstant.ENTITY_TYPE_USER, id));
            followers.add(follower);
        }
        return followers;
    }

    /** 查看用户 对某类实体 的关注列表 */
    public List<Map<String, Object>> queryFollowing(int userId, int entityType, int page, int size) {
        int offset = (page - 1) * size;

        String key = RedisKeyUtil.getFollowingKey(userId, entityType);
        BoundZSetOperations ops = redisTemplate.boundZSetOps(key);
        Set<Integer> ids = ops.reverseRange(offset, offset + size - 1);

        List<Map<String, Object>> followers = new ArrayList<>();
        for (Integer id : ids) {
            Map<String, Object> follower = new HashMap<>();
            follower.put("user", userService.queryUserById(id));
            follower.put("followTime", new Date(ops.score(id).longValue()));
            follower.put("followStatus", queryFollowStatus(CommunityConstant.ENTITY_TYPE_USER, id));
            followers.add(follower);
        }
        return followers;
    }
}
