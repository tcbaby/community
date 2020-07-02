package com.tcbaby.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tcbaby
 * @date 20/05/04 15:32
 */
@MapperScan(basePackages = "com.tcbaby.community.mapper")
@SpringBootApplication
public class CommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class);
    }
}
