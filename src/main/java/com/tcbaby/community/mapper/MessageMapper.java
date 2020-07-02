package com.tcbaby.community.mapper;

import com.tcbaby.community.pojo.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tcbaby
 * @date 20/05/13 10:29
 */
@Repository
public interface MessageMapper {

    /**
     * 统计用户的 总会话数
     * @param userId
     * @return
     */
    int countConversationByUserId(int userId);

    /**
     * 统计用户的未读私信数
     * @param userId
     * @return
     */
    int countUnreadLetterByUserId(int userId);

    /**
     * 分页 获取用户的会话列表  每个会话加载最后一条消息
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    List<Message> selectConversationsByUserId(int userId, int offset, int size);

    /**
     * 统计某个会话的消息数
     * @param userId  0: 统计会话的所有消息， 非0： 统计会话中该用户的发言数
     * @param conversationId
     * @return
     */
    int countConversationMessage(int userId, String conversationId);

    /**
     * 统计某个会话的未读消息数
     * @param userId
     * @param conversationId
     * @return
     */
    int countConversationUnreadMessage(int userId, String conversationId);

    /**
     * 分页 查询某个会话的消息
     * @param conversationId
     * @return
     */
    List<Message> queryMessageByConversationId(String conversationId, int offset, int size);


    /** 批量更新 Message 的状态 */
    int updateMessageStatus(@Param("messageIds") List<Integer> messageIds, @Param("status") int status);

    /** 保存一条 Message */
    int saveMessage(Message message);
}
