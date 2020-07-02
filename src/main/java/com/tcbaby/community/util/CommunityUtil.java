package com.tcbaby.community.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author tcbaby
 * @date 20/05/05 11:48
 */
public class CommunityUtil {

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String md5(String data, String salt) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(salt)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex((data + salt).getBytes());
    }
}
