package com.tcbaby.community.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tcbaby
 * @date 20/05/14 11:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    /** 点赞状态 */
    private int likeStatus;
    /** 点赞数 */
    private long totalLike;
}
