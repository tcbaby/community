package com.tcbaby.community.web;

import com.tcbaby.community.annotation.LoginRequired;
import com.tcbaby.community.service.CommentService;
import com.tcbaby.community.service.DiscussPostService;
import com.tcbaby.community.util.CommunityConstant;
import com.tcbaby.community.vo.CommentVo;
import com.tcbaby.community.vo.DiscussPostVo;
import com.tcbaby.community.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tcbaby
 * @date 20/05/11 7:32
 */
@Controller
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/discuss/{id}")
    public String getDiscussDetailPage(Model model, @PathVariable("id") int id, Page page) {
        // 获取帖子详细信息
        DiscussPostVo postVo = discussPostService.queryDiscussPostById(id);
        // 设置分页加载评论
        page.setTotal(postVo.getCommentCount());
        page.setPath("/discuss/" + id);
        // 评论的详细信息
        List<CommentVo> commentVos = commentService.queryCommentsByEntityType(
                CommunityConstant.ENTITY_TYPE_POST, postVo.getId(), page.getPage(), page.getSize());
        postVo.setComments(commentVos);
        model.addAttribute("postVo", postVo);
        return "site/discuss-detail";
    }

    @LoginRequired
    @ResponseBody
    @PostMapping("/discuss")
    public ResponseEntity<Void> saveDiscussPost(@RequestParam("title") String title, @RequestParam("title") String content) {
        discussPostService.saveDiscussPost(title, content);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}