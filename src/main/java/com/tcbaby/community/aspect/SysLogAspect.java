package com.tcbaby.community.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tcbaby
 * @date 20/05/10 19:28
 */
@Slf4j
@Aspect
@Component
public class SysLogAspect {

    @Pointcut("execution(* com.tcbaby.community.web.*.*(..))")
    public void controllerPointcut() {
    }

    @Before("controllerPointcut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String ip = request.getRemoteAddr();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String visitTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        log.info("ip:{}, 时间：{}，访问：{}.{}()", ip, visitTime, className, methodName);
    }
}
