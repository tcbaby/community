package com.tcbaby.community.service;

import com.tcbaby.community.mapper.DiscussPostMapper;
import com.tcbaby.community.pojo.DiscussPost;
import com.tcbaby.community.pojo.HostHolder;
import com.tcbaby.community.util.CommunityConstant;
import com.tcbaby.community.util.SensitiveFilter;
import com.tcbaby.community.vo.DiscussPostVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tcbaby
 * @date 20/05/04 15:40
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;

    public List<DiscussPostVo> queryDiscussPosts(int uid, int page, int size) {
        List<DiscussPost> posts = discussPostMapper.queryDiscussPosts(uid, (page - 1) * size, size);
        if (posts == null) {
            return null;
        }
        return posts.stream().map(post -> {
            DiscussPostVo postVo = new DiscussPostVo();
            BeanUtils.copyProperties(post, postVo);
            // 发帖人
            postVo.setOwnner(userService.queryUserById(postVo.getUserId()));
            // 帖子获赞数
            postVo.setTotalLike(likeService.countEntityLike(CommunityConstant.ENTITY_TYPE_POST, postVo.getId()));
            return postVo;
        }).collect(Collectors.toList());
    }

    public int queryDiscussPostRows(int uid) {
        return discussPostMapper.queryDiscussPostRows(uid);
    }

    public DiscussPostVo queryDiscussPostById(int id) {
        DiscussPost post = discussPostMapper.queryDiscussPostById(id);
        DiscussPostVo postVo = new DiscussPostVo();
        BeanUtils.copyProperties(post, postVo);
        // 发帖人
        postVo.setOwnner(userService.queryUserById(post.getUserId()));
        // 获赞数
        postVo.setTotalLike(likeService.countEntityLike(CommunityConstant.ENTITY_TYPE_POST, postVo.getId()));
        // 用户的点赞状态
        postVo.setLikeStatus(likeService.queryEntityLikeStatus(CommunityConstant.ENTITY_TYPE_POST, postVo.getId()));
        return postVo;
    }

    public void saveDiscussPost(String title, String content) {
        if (StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("title、content不能为空！");
        }
        // 转义html代码、过滤敏感词
        title = sensitiveFilter.filter(HtmlUtils.htmlEscape(title));
        content = sensitiveFilter.filter(HtmlUtils.htmlEscape(content));
        // 持久化
        DiscussPost post = new DiscussPost();
        post.setUserId(HostHolder.getUser().getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        post.setStatus(0);
        discussPostMapper.saveDiscussPost(post);
    }
}