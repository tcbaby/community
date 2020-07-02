package com.tcbaby.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author tcbaby
 * @date 20/05/13 10:24
 */
@Data
public class Message {

    private int id;

    /** 发消息的用户Id   fromId=1保留，为系统推送消息 */
    private int fromId;
    /** 接收消息的用户Id */
    private int toId;

    /** 会话Id  冗余字段 就是fromId拼上toId，小在前 */
    private String conversationId;

    private String content;

    /** 0-未读  1-已读  2-删除 */
    private int status;

    private Date createTime;
}
