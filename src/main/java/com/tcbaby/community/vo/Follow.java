package com.tcbaby.community.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tcbaby
 * @date 20/05/14 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    /** 关注状态 1-关注，0-没关注 */
    private int followStatus;
    /** 总关注数 */
    private long totalFollowers;
}
