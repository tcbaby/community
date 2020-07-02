package com.tcbaby.community.web;

import com.tcbaby.community.pojo.Comment;
import com.tcbaby.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author tcbaby
 * @date 20/05/12 16:14
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}")
    public String addComment(@PathVariable("postId") int postId, Comment comment) {
        commentService.addComment(comment);
        return "redirect:/discuss/" + postId;
    }
}
