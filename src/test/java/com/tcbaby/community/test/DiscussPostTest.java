package com.tcbaby.community.test;

import com.tcbaby.community.service.CommentService;
import com.tcbaby.community.service.DiscussPostService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author tcbaby
 * @date 20/05/12 14:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscussPostTest {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private CommentService commentService;

}
