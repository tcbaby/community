package com.tcbaby.community.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author tcbaby
 * @date 2020/7/21
 */
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private int id;
    private String username;

    public int getId() {
        return id;
    }

    public UserInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserInfo setUsername(String username) {
        this.username = username;
        return this;
    }
}
