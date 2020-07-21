package com.tcbaby.community.web.intercepter;

import com.tcbaby.community.config.JwtProperties;
import com.tcbaby.community.pojo.HostHolder;
import com.tcbaby.community.pojo.User;
import com.tcbaby.community.service.MessageService;
import com.tcbaby.community.service.UserService;
import com.tcbaby.community.util.CookieUtil;
import com.tcbaby.community.util.JwtUtils;
import com.tcbaby.community.util.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tcbaby
 * @date 20/05/06 9:00
 */
@Slf4j
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private JwtProperties jwtProperties;

    /** Controller方法执行前 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 认证用户信息
        String token = CookieUtil.getCookie(request, jwtProperties.getCookieName());
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getSecret());
        if (userInfo != null) {
            HostHolder.setUser(userService.queryUserById(userInfo.getId()));
        }
        // 解析后直接放行，不做拦截处理
        return true;
    }

    /** Controller方法执行后，渲染视图前（TemplateEngine前） */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = HostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginuser", user);
            // 设置私信数
            modelAndView.addObject("totalLetter", messageService.countUnreadLetterByUserId(user.getId()));
            HostHolder.clean();
        }
    }

    /** 最后执行 */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
