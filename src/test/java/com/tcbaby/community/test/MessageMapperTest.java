package com.tcbaby.community.test;

import com.tcbaby.community.mapper.MessageMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tcbaby
 * @date 20/05/13 12:16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testMessage() {
        int totalConversation = messageMapper.countConversationByUserId(111);
        System.out.println(totalConversation);

        int totalMessage = messageMapper.countConversationMessage(0, "111_112");
        System.out.println(totalMessage);

        int totalUnReadLetter = messageMapper.countUnreadLetterByUserId(111);
        System.out.println(totalUnReadLetter);
    }
}
