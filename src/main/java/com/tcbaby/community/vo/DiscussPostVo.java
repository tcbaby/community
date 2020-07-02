package com.tcbaby.community.vo;

import com.tcbaby.community.pojo.DiscussPost;
import com.tcbaby.community.pojo.User;
import lombok.Data;

import java.util.List;

/**
 * @author tcbaby
 * @date 20/05/11 11:58
 */
@Data
public class DiscussPostVo extends DiscussPost {

    /** 发帖的用户 */
    private User ownner;

    /** 点赞数 */
    private long totalLike;
    /** 点赞状态  判断当前用户是否点赞了 */
    private int likeStatus;

    /** 评论列表 */
    private List<CommentVo> comments;
}
