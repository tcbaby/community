package com.tcbaby.community.test;

import com.tcbaby.community.util.MailClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

/**
 * @author tcbaby
 * @date 20/05/04 23:01
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class MailClientTest {

    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testSendTextMail() throws InterruptedException {
        String[] arr = {"垃圾", "辣鸡", "哈麻批", "强奸犯"};
        for (int i = 0; i < 15; i++) {
            int idx = new Random().nextInt(4);
            String sub = arr[idx] + "您好！";
            String content = "<font color='red'>" + arr[idx] + "</font>";
            mailClient.sendMail("1832656473@qq.com", sub, content);
            Thread.sleep(idx * 800);
        }
    }

    @Test
    public void testSendHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "test");
        String content = templateEngine.process("mail/test", context);
        mailClient.sendMail("794148917@qq.com", "test send html email", content);
    }
}
