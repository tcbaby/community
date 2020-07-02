package com.tcbaby.community.web;

import com.google.code.kaptcha.Producer;
import com.tcbaby.community.annotation.LoginRequired;
import com.tcbaby.community.config.JwtProperties;
import com.tcbaby.community.pojo.User;
import com.tcbaby.community.service.UserService;
import com.tcbaby.community.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author tcbaby
 * @date 20/05/05 11:04
 */
@Slf4j
@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private Producer producer;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JwtProperties jwtProperties;

    @GetMapping("/register")
    public String getRegisterPage() {
        return "site/register";
    }

    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, String> map = userService.register(user);
        if (map != null) {
            model.addAllAttributes(map);
            return "site/register";
        }
        model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
        model.addAttribute("target", "login");
        return "site/operate-result";
    }

    @GetMapping("/activation/{uid}/{code}")
    public String activation(Model model, @PathVariable("uid") int uid, @PathVariable("code") String code) {
        int status = userService.activation(uid, code);
        if (status == CommunityConstant.ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "您已成功激活你的账户！");
            model.addAttribute("target", "/login");
        } else if (status == CommunityConstant.ACTIVATION_REPEAT) {
            model.addAttribute("msg", "您的账号已经激活过，无需重复激活！");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败，激活链接不正确！");
            model.addAttribute("target", "/index");
        }
        return "site/operate-result";
    }

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response) {
        // 生成验证码
        String captcha = producer.createText();
        BufferedImage image = producer.createImage(captcha);
        // 将验证码存入redis
        String mark = CommunityUtil.generateUUID();
        String key = RedisKeyUtil.getCaptchaKey(mark);
        redisTemplate.opsForValue().set(key, captcha, 60, TimeUnit.SECONDS);
        // 设置cookie
        CookieUtil.addCookie(response, "CAPTCHA_MARK", mark, 60);
        // 将验证码图片写入response中
        response.setContentType("image/png");
        try {
            ImageIO.write(image, "png", response.getOutputStream());
        } catch (IOException e) {
            log.error("响应验证码失败！: {}", e.getMessage());
        }
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/site/login";
    }

    @PostMapping("/login")
    public String login(Model model, HttpServletResponse response, @CookieValue("CAPTCHA_MARK") String captchaMark,
                        User user, String verifycode, Boolean remember) throws Exception {
        // 验证验证码
        String key = RedisKeyUtil.getCaptchaKey(captchaMark);
        String captcha = (String) redisTemplate.opsForValue().get(key);
        if (!StringUtils.equalsIgnoreCase(captcha, verifycode)) {
            model.addAttribute("verifycodeMsg", "验证码不正确！");
            return "site/login";
        }
        int expire = remember ? CommunityConstant.MAX_EXPIRE_SECONDS : CommunityConstant.MIN_EXPIRE_SECONDS;
        Map<String, String> map = userService.login(user);
        model.addAllAttributes(map);
        if (map == null || map.size() == 0) {
            // 设置token
            String token = JwtUtils.generateToken(user, jwtProperties.getPrivateKey(), expire);
            CookieUtil.addCookie(response, jwtProperties.getCookieName(), token, expire);
            return "redirect:/";
        }
        return "site/login";
    }

    @LoginRequired
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 删除客户端的token
        CookieUtil.delCookie(response, jwtProperties.getCookieName());
        return "redirect:/";
    }
}
