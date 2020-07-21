package com.tcbaby.community.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * @author tcb
 * @date 2020/7/21
 */
public class JwtUtils {

    /** 加密算法 */
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 生成token
     * @param userInfo 用户信息
     * @param secret 密钥
     * @param expireMinutes 有效分钟数
     * @return
     */
    public static String generateToken(UserInfo userInfo, String secret, int expireMinutes) {
        return Jwts.builder()
                .claim(JwtConstans.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstans.JWT_KEY_USERNAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(signatureAlgorithm, secret)
                .compact();
    }

    /**
     * 解析token
     * @param token
     * @param secret
     * @return
     */
    private static Claims parserToken(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取token中的用户信息
     * @param token
     * @param secret
     * @return
     */
    public static UserInfo getInfoFromToken(String token, String secret) {
        if (StringUtils.isNotBlank(token)) {
            Claims body = parserToken(token, secret);
            return new UserInfo()
                    .setId(Integer.valueOf(body.get(JwtConstans.JWT_KEY_ID).toString()))
                    .setUsername(body.get(JwtConstans.JWT_KEY_USERNAME).toString());
        }
        return null;
    }
}