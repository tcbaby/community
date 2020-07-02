package com.tcbaby.community.vo;

import com.tcbaby.community.pojo.Comment;
import com.tcbaby.community.pojo.User;
import lombok.Data;

import java.util.List;

/**
 * @author tcbaby
 * @date 20/05/11 11:54
 */
@Data
public class CommentVo extends Comment {

    /** 评论人 */
    private User ownner;

    /**
     * 目标用户   只有当entityType为2时，才有
     */
    private User target;

    /** 获赞数 */
    private long totalLike;
    /** 点赞状态  判断当前用户是否点赞了 */
    private int likeStatus;

    /**
     * 回复列表    只有当entityType为1时，才有
     */
    private List<CommentVo> replies;
}
