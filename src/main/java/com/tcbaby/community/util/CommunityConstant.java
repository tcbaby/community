package com.tcbaby.community.util;

/**
 * @author tcbaby
 * @date 20/05/05 16:44
 */
public interface CommunityConstant {
    /** 激活状态 */
    int ACTIVATION_SUCCESS = 1;
    int ACTIVATION_REPEAT = 2;
    int ACTIVATION_ERROR = 0;

    /** 用户 保持登录时间 */
    int MIN_EXPIRE_SECONDS = 30 * 60;
    int MAX_EXPIRE_SECONDS= 24 * 60 * 60;

    /** 实体类型： 帖子 */
    int ENTITY_TYPE_POST = 1;
    /** 实体类型： 评论 */
    int ENTITY_TYPE_COMMENT = 2;
    /** 实体类型：用户 */
    int ENTITY_TYPE_USER = 3;
}