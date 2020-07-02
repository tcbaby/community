package com.tcbaby.community.mapper;

import com.tcbaby.community.pojo.DiscussPost;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tcbaby
 * @date 20/05/04 15:39
 */
@Repository
public interface DiscussPostMapper {

    List<DiscussPost> queryDiscussPosts(int uid, int offset, int size);

    int queryDiscussPostRows(int uid);

    DiscussPost queryDiscussPostById(int id);

    int saveDiscussPost(DiscussPost post);

    int incrementCommentCountById(int id);
}
