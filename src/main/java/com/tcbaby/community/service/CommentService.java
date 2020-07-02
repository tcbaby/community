package com.tcbaby.community.service;

import com.tcbaby.community.mapper.CommentMapper;
import com.tcbaby.community.mapper.DiscussPostMapper;
import com.tcbaby.community.pojo.Comment;
import com.tcbaby.community.pojo.HostHolder;
import com.tcbaby.community.util.CommunityConstant;
import com.tcbaby.community.util.SensitiveFilter;
import com.tcbaby.community.vo.CommentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tcbaby
 * @date 20/05/11 12:05
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private LikeService likeService;

    /**
     * 加载评论、评论人
     * @param entityType
     * @param entityId
     * @param page
     * @param size
     * @return
     */
    public List<CommentVo> queryCommentsByEntityType (int entityType, int entityId, int page, int size) {
        List<Comment> comments = commentMapper.selectCommentsByEntityType(entityType, entityId, (page - 1) * size, size);

        return comments.stream().map(c -> {
            CommentVo cvo = new CommentVo();
            BeanUtils.copyProperties(c, cvo);
            // 评论人
            cvo.setOwnner(userService.queryUserById(cvo.getUserId()));
            // 获赞数
            cvo.setTotalLike(likeService.countEntityLike(cvo.getEntityType(), cvo.getEntityId()));
            // 用户的点赞状态
            cvo.setLikeStatus(likeService.queryEntityLikeStatus(cvo.getEntityType(), cvo.getEntityId()));
            if (entityType == CommunityConstant.ENTITY_TYPE_POST) {
                // 加载回复列表
                List<CommentVo> replies = queryCommentsByEntityType(CommunityConstant.ENTITY_TYPE_COMMENT, c.getId(), 1, Short.MAX_VALUE);
                cvo.setReplies(replies);
            } else if (entityType == CommunityConstant.ENTITY_TYPE_COMMENT) {
                // 加载目标用户
                cvo.setTarget(userService.queryUserById(cvo.getTargetId()));
            }
            return cvo;
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void addComment (Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("comment不能为null");
        }
        comment.setUserId(HostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        comment.setContent(sensitiveFilter.filter(HtmlUtils.htmlEscape(comment.getContent())));
        commentMapper.saveComment(comment);
        if (CommunityConstant.ENTITY_TYPE_POST == comment.getEntityType()) {
            // 修改discussPost中冗余的commentCount
            discussPostMapper.incrementCommentCountById(comment.getEntityId());
        }
    }
}
