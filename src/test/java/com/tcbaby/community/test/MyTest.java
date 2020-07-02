package com.tcbaby.community.test;

import com.tcbaby.community.mapper.UserMapper;
import com.tcbaby.community.pojo.User;
import com.tcbaby.community.util.CommunityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * @author tcbaby
 * @date 20/05/04 17:30
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testDB() {
        User user = userMapper.selectById(101);
        log.info("user: {}", user);
        System.out.println(user);
    }

    @Test
    public void testReg() {
        String reg = "^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\\.[a-z]{2,}$";

        System.out.println("116@qq.com".matches(reg));
        System.out.println("tt@tt.com".matches(reg));
        System.out.println("tt".matches(reg));
    }

    @Test
    public void testMD5() {
        User user = userMapper.selectByUsername("tt");
        String password = CommunityUtil.md5("tt", user.getSalt());
        System.out.println(user.getPassword());
        System.out.println(password);
        System.out.println(StringUtils.equals(password, user.getPassword()));
    }

    @Test
    public void testToken() {
        String token = CommunityUtil.generateUUID();
        token = 101 + "-" + token;

        System.out.println(token);
        System.out.println(Arrays.toString(token.split("-", 2)));
        String substring = StringUtils.substringBefore(token, "-");
        System.out.println(substring);

        System.out.println(Integer.valueOf("424fx"));
    }
}

