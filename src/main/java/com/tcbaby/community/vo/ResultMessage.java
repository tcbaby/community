package com.tcbaby.community.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author tcbaby
 * @date 20/05/10 17:55
 */
@Data
public class ResultMessage {
    private int code;
    private String msg;
    private long stamp;

    private Map<String, Object> detail;

    public ResultMessage() {
    }

    public ResultMessage(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.stamp = System.currentTimeMillis();
    }

    public ResultMessage(int code, String msg, Map<String, Object> detail) {
        this(code, msg);
        this.detail = detail;
    }
}
