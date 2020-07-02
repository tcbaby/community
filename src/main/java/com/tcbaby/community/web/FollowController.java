package com.tcbaby.community.web;

import com.tcbaby.community.annotation.LoginRequired;
import com.tcbaby.community.pojo.User;
import com.tcbaby.community.service.FollowService;
import com.tcbaby.community.service.UserService;
import com.tcbaby.community.util.CommunityConstant;
import com.tcbaby.community.vo.Follow;
import com.tcbaby.community.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Map;

/**
 * @author tcbaby
 * @date 20/05/14 15:58
 */
@Controller
public class FollowController {

    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;

    @LoginRequired
    @PutMapping("/follow")
    public ResponseEntity<Follow> followUnFollow(int entityType, int entityId) {
        int followStatus = followService.followOrUnfollow(entityType, entityId);
        long totalFollowers = followService.countFollowers(entityType, entityId);
        return ResponseEntity.ok(new Follow(followStatus, totalFollowers));
    }

    @GetMapping("/followers/{uid}")
    public String getFollowerPage(Model model, Page page, @PathVariable("uid") int uid) {
        User user = userService.queryUserById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在！");
        }
        // 设置分页信息
        page.setTotal(followService.countFollowers(CommunityConstant.ENTITY_TYPE_USER, uid));
        page.setPath("/followers/" + uid);
        // 获取关注者
        List<Map<String, Object>> followers = followService.queryFollowers(
                CommunityConstant.ENTITY_TYPE_USER, uid, page.getPage(), page.getSize());
        model.addAttribute("followers", followers);
        model.addAttribute("user", user);
        return "site/follower";
    }

    @GetMapping("/following/{uid}")
    public String getFollowingPage(Model model, Page page, @PathVariable("uid") int uid) {
        User user = userService.queryUserById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在！");
        }
        // 设置分页信息
        page.setTotal(followService.countFollowing(uid, CommunityConstant.ENTITY_TYPE_USER));
        page.setPath("/following/" + uid);
        // 获取用户的关注列表
        List<Map<String, Object>> following = followService.queryFollowing(
                uid, CommunityConstant.ENTITY_TYPE_USER, page.getPage(), page.getSize());
        model.addAttribute("following", following);
        model.addAttribute("user", user);
        return "site/followee";
    }
}
