package com.tcbaby.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author tcbaby
 * @date 20/05/04 15:36
 */
@Data
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;

    /** 帖子类型： 0-普通; 1-置顶 */
    private int type;

    /** 帖子状态： 0-正常; 1-精华; 2-拉黑 */
    private int status;

    /** 评论数 */
    private int commentCount;

    private double score;

    private Date createTime;
}
