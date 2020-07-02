package com.tcbaby.community.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author tcbaby
 * @date 20/05/04 15:36
 */
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
