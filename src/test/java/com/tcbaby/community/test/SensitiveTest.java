package com.tcbaby.community.test;

import com.tcbaby.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.util.HtmlUtils;

/**
 * @author tcbaby
 * @date 20/05/09 17:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    public void testFilter() {
        String content = "❤嫖❤娼❤啊！，嫖啊，❤赌❤啊";
        String res = sensitiveFilter.filter(content);
        System.out.println(content);
        System.out.println(res);
    }

    @Test
    public void testHtmlUtils() {
        String content = "<font color='red'>haha</font>";
        String res = HtmlUtils.htmlEscape(content);
        System.out.println(content);
        System.out.println(res);
    }
}
