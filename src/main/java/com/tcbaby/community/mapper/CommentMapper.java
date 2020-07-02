package com.tcbaby.community.mapper;

import com.tcbaby.community.pojo.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tcbaby
 * @date 20/05/11 12:04
 */
@Repository
public interface CommentMapper {

    /**
     * 根据评论类型 分页查看评论
     * @param entityType
     * @param entityId
     * @param offset
     * @param size
     * @return
     */
    List<Comment> selectCommentsByEntityType(int entityType, int entityId, int offset, int size);

    int saveComment(Comment comment);
}
