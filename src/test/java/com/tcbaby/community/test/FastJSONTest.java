package com.tcbaby.community.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcbaby.community.vo.ResultMessage;
import org.junit.Test;

/**
 * @author tcbaby
 * @date 20/05/11 10:28
 */
public class FastJSONTest {

    @Test
    public void test1() {
        ResultMessage success = new ResultMessage(200, "success");
        String string = JSON.toJSONString(success);
        System.out.println(string);
    }

    @Test
    public void test2() {
        JSONObject json = new JSONObject();
        json.put("code", "404");
        json.put("msg", "not found!");
        System.out.println(json.toString());
    }
}
