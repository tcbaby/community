package com.tcbaby.community.util;

/**
 * @author tcbaby
 * @date 20/05/14 9:51
 */
public final class RedisKeyUtil {
    private static final String SPLIT = ":";

    private static final String PREFIX_CAPTCHA = "captcha";
    private static final String PRIFIX_USER = "user";

    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";

    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_FOLLOWING = "following";

    public static String getCaptchaKey(String mark) {
        return PREFIX_CAPTCHA + SPLIT + mark;
    }

    public static String getUserKey(int userId) {
        return PRIFIX_USER + SPLIT + userId;
    }

    /** 实体拥有的点赞 like:entity:entityType:entityId  --> set[...userId] */
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    /** 用户的点赞数 like:user:userId --> int */
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    /** 实体拥有的粉丝  follower:entityType:entityId --> zset(userId, now) */
    public static String getFollowerKey (int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    /** 用户关注的实体 following:userId:entityType --> zset(entityId, now) */
    public static String getFollowingKey (int userId, int entityType) {
        return PREFIX_FOLLOWING + SPLIT + userId + SPLIT + entityType;
    }
}
