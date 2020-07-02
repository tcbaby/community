package com.tcbaby.community.web;

import com.tcbaby.community.annotation.LoginRequired;
import com.tcbaby.community.service.LikeService;
import com.tcbaby.community.vo.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author tcbaby
 * @date 20/05/14 10:19
 */
@RestController
public class LikeController {

    @Autowired
    private LikeService likeService;

    @LoginRequired
    @PutMapping("/like")
    public ResponseEntity<Like> likeOrUnlike(int entityUserId, int entityType, int entityId) {
        int likeStatus = likeService.likeOrUnlike(entityUserId, entityType, entityId);
        long totalLike = likeService.countEntityLike(entityType, entityId);
        return ResponseEntity.ok(new Like(likeStatus, totalLike));
    }
}
