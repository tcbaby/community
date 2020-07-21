package com.tcbaby.community.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "community.jwt")
public class JwtProperties {
    private String secret;
    private String cookieName;
    private int minExpireMinutes;
    private int maxExpireMinutes;

    public int getExpire(boolean remember) {
        return remember ? maxExpireMinutes : minExpireMinutes;
    }
}
