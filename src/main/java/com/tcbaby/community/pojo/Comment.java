package com.tcbaby.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author tcbaby
 * @date 20/05/11 11:55
 */
@Data
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
}
