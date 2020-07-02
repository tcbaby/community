package com.tcbaby.community.vo;

import com.tcbaby.community.pojo.Message;
import lombok.Data;

/**
 * @author tcbaby
 * @date 20/05/13 11:15
 */
@Data
public class Conversation {
    /** 会话的最后一条消息 */
    private Message lastMessage;
    /** 总消息数 */
    private int totalMessage;
    /** 未读消息数 */
    private int totalUnReadMessage;
}
