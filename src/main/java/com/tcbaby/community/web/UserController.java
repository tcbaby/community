package com.tcbaby.community.web;

import com.tcbaby.community.annotation.LoginRequired;
import com.tcbaby.community.service.FollowService;
import com.tcbaby.community.service.ImageService;
import com.tcbaby.community.service.LikeService;
import com.tcbaby.community.service.UserService;
import com.tcbaby.community.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author tcbaby
 * @date 20/05/06 16:05
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/headImage")
    public String uploadHeadImage(MultipartFile headImage, Model model) {
        Map<String, String> map = userService.updateHeaderUrl(headImage);
        if (map != null) {
            model.addAllAttributes(map);
            return "/site/setting";
        }
        return "redirect:/";
    }

    @GetMapping("/headImage/{filename}")
    public void getHeadImage(@PathVariable("filename") String filename, Model model, HttpServletResponse response) {
        try {
            // 将图片写入response中
            String suffix = filename.substring(filename.lastIndexOf(".") + 1);
            response.setContentType("image/" + suffix);
            imageService.getImage(filename, response.getOutputStream());
        } catch (IOException e) {
            log.error("获取图片失败：{}", e.getMessage());
        }
    }

    /** 更新密码，   这里用post代替put请求 */
    @LoginRequired
    @PostMapping("/pd")
    public String updatePassowrd(Model model, String oldPassword, String newPassword) {
        Map<String, String> map = userService.updatePassword(oldPassword, newPassword);
        if (map == null) {
            return "redirect:/";
        }
        model.addAllAttributes(map);
        return "/site/setting";
    }

    @GetMapping("/profile/{uid}")
    public String getProfilePage(Model model, @PathVariable("uid") int uid) {
        model.addAttribute("user", userService.queryUserById(uid));
        model.addAttribute("totalLike", likeService.countUserLike(uid));
        model.addAttribute("totalFollowing",
                followService.countFollowing(uid, CommunityConstant.ENTITY_TYPE_USER));
        model.addAttribute("totalFollowers",
                followService.countFollowers(CommunityConstant.ENTITY_TYPE_USER, uid));
        model.addAttribute("followStatus",
                followService.queryFollowStatus(CommunityConstant.ENTITY_TYPE_USER, uid));
        return "site/profile";
    }
}