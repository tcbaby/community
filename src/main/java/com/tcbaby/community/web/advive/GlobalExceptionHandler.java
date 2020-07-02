package com.tcbaby.community.web.advive;

import com.alibaba.fastjson.JSON;
import com.tcbaby.community.vo.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tcbaby
 * @date 20/05/10 17:45
 */
@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("异常：{}", e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            log.error(element.toString());
        }
        String requestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(requestedWith)) {
            // 异步请求 响应错误信息
            response.setContentType("application/json;charset=utf8");
            response.setStatus(500);
            response.getWriter().write(JSON.toJSONString(new ResultMessage(500, e.getMessage())));
        } else {
            // 转发到错误页面  template/error/目录下对应的页面
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
