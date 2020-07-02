package com.tcbaby.community.service;

import com.tcbaby.community.mapper.UserMapper;
import com.tcbaby.community.pojo.HostHolder;
import com.tcbaby.community.pojo.User;
import com.tcbaby.community.util.CommunityConstant;
import com.tcbaby.community.util.CommunityUtil;
import com.tcbaby.community.util.MailClient;
import com.tcbaby.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author tcbaby
 * @date 20/05/04 15:39
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${community.domain}")
    private String domian;

    @Autowired
    private ImageService imageService;

    public User queryUserById(int id) {
        // 先从缓存中区，没有则创建缓存
        User user = getCache(id);
        return user != null ? user : initCache(id);
    }

    private final User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String userKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(userKey, user, 60, TimeUnit.MINUTES);
        return user;
    }

    private final User getCache(int userId) {
        String userKey = RedisKeyUtil.getUserKey(userId);
        User user = (User) redisTemplate.opsForValue().get(userKey);
        return user;
    }

    private final void cleanCache(int userId) {
        String userKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(userKey);
    }

    public Map<String, String> register(User user) {
        Map<String, String> map = new HashMap<>();
        // 校验
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())
                || !user.getEmail().matches("^[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-_.][a-zA-Z0-9]+)*\\.[a-z]{2,}$")) {
            map.put("emailMsg", "邮箱不正确！");
            return map;
        }
        User u = userMapper.selectByUsername(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "用户名已经存在!");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该email已经使用！");
            return map;
        }
        // 存入数据库
        String salt = CommunityUtil.generateUUID().replace("-", "");
        String password = CommunityUtil.md5(user.getPassword(), salt);
        String icon = String.format("http://images.nowcoder.com/head/%dt.png",  new Random().nextInt(1000));
        String activationCode = CommunityUtil.generateUUID();
        user.setSalt(salt);
        user.setPassword(password);
        user.setHeaderUrl(icon);
        user.setActivationCode(activationCode);
        user.setStatus(0);
        user.setType(0);
        user.setCreateTime(new Date());
        userMapper.save(user);
        cleanCache(user.getId());
        // 发送邮件、提醒用户激活
        String activationUrl = domian + "/activation/" + user.getId() + "/" + activationCode;
        Context context = new Context();
        context.setVariable("username", user.getUsername());
        context.setVariable("activationUrl", activationUrl);
        String content = templateEngine.process("mail/activation", context);
        mailClient.sendMail(user.getEmail(), "账户激活", content);
        System.out.println("activationUrl = " + activationUrl);
        return null;
    }

    public int activation(int uid, String code) {
        User user = userMapper.selectById(uid);
        if (user.getStatus() == 1) {
            return CommunityConstant.ACTIVATION_REPEAT;
        }
        if (user.getActivationCode().equals(code)) {
            userMapper.updateStatusById(uid, 1);
            cleanCache(uid);
            return CommunityConstant.ACTIVATION_SUCCESS;
        }
        return CommunityConstant.ACTIVATION_ERROR;
    }

    public Map<String, String> login(User user) {
        Map<String, String> map = new HashMap<>();
        // 验证
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        User u = userMapper.selectByUsername(user.getUsername());
        if (u == null) {
            map.put("usernameMsg", "用户不存在！");
            return map;
        }
        if (!StringUtils.equals(u.getPassword(), CommunityUtil.md5(user.getPassword(), u.getSalt()))) {
            map.put("passwordMsg", "密码不正确！");
            return map;
        }
        if (u.getStatus() != 1) {
            map.put("usernameMsg", "用户为激活！)");
        }
        user.setId(u.getId());
        return map;
    }

    public Map<String, String> updateHeaderUrl(MultipartFile headImage) {
        Map<String, String> map = imageService.saveImage(headImage);
        String headerUrl = map.remove("imageUrl");
        if (StringUtils.isBlank(headerUrl)) {
            return map;
        }
        userMapper.updateHeaderUrlById(HostHolder.getUser().getId(), headerUrl);
        return null;
    }

    public Map<String, String> updatePassword(String oldPassword, String newPassword) {
        Map<String, String> map = new HashMap<String, String>();
        // 校验
        if (StringUtils.isBlank(oldPassword)) {
            map.put("oldPasswordMsg", "原密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(newPassword)) {
            map.put("newPasswordMsg", "新密码不能为空！");
            return map;
        }
        User user = HostHolder.getUser();
        oldPassword = CommunityUtil.md5(oldPassword, user.getSalt());
        if (!oldPassword.equals(user.getPassword())) {
            map.put("oldPasswordMsg", "密码不不正确！");
            return map;
        }
        // 更新密码
        int count = userMapper.updatePasswordById(user.getId(), CommunityUtil.md5(newPassword, user.getSalt()));
        if (count != 1) {
            map.put("newPasswordMsg", "密码更新失败，请稍后重试！");
            return map;
        }
        cleanCache(user.getId());
        return null;
    }

    public User queryUserByUsername(String toUserName) {
        return userMapper.selectByUsername(toUserName);
    }
}