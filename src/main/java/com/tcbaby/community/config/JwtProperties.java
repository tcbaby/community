package com.tcbaby.community.config;

import com.tcbaby.community.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
@Data
@ConfigurationProperties(prefix = "community.jwt")
public class JwtProperties {

    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private int expire;
    private String cookieName;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * 构造方法执行前执行
     */
    @PostConstruct
    public void init () {
        try {
            // 生成公私钥
            RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            // 获取公私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            log.error("初始化公私钥失败!", e);
        }
    }
}
