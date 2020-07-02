package com.tcbaby.community.service;

import com.tcbaby.community.mapper.MessageMapper;
import com.tcbaby.community.pojo.HostHolder;
import com.tcbaby.community.pojo.Message;
import com.tcbaby.community.pojo.User;
import com.tcbaby.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

/**
 * @author tcbaby
 * @date 20/05/13 10:29
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> queryConversationsByUserId(int userId, int page, int size) {
        return messageMapper.selectConversationsByUserId(userId, (page - 1) * size, size);
    }

    public int countConversationByUserId(int userId) {
        return messageMapper.countConversationByUserId(userId);
    }

    public int countUnreadLetterByUserId(int userId) {
        return messageMapper.countUnreadLetterByUserId(userId);
    }

    public int countConversationMessage(int userId, String conversationId) {
        return messageMapper.countConversationMessage(userId, conversationId);
    }

    public int countConversationUnreadMessage(int userId, String conversationId) {
        return messageMapper.countConversationUnreadMessage(userId, conversationId);
    }

    public List<Message> queryMessageByConversationId(String conversationId, int page, int size) {
        return messageMapper.queryMessageByConversationId(conversationId, (page - 1) * size, size);
    }

    public void updateMessageStatusToReaded(List<Integer> messageIds) {
        if (CollectionUtils.isEmpty(messageIds)) {
            return;
        }
        messageMapper.updateMessageStatus(messageIds, 1);
    }

    public void sendMessage(String toUserName, String content) {
        User to = userService.queryUserByUsername(toUserName);
        if (to == null) {
            throw new IllegalArgumentException("私信用户不存在！");
        }

        int toId = to.getId();
        int fromId = HostHolder.getUser().getId();
        String conversationId = (fromId < toId) ? fromId + "_" + toId : toId + "_" + fromId;
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setConversationId(conversationId);
        message.setContent(sensitiveFilter.filter(HtmlUtils.htmlEscape(content)));
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageMapper.saveMessage(message);
    }
}
