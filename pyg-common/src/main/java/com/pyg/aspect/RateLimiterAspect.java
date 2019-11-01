package com.pyg.aspect;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.pyg.annotation.AnRateLimiter;
import com.pyg.utils.PygResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Title RateLimiterAspect
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-31 16:38
 */
@Aspect
@Component
public class RateLimiterAspect {
    /**
     * 不同的方法存放不同的令牌桶
     */
    private final Map<String, RateLimiter> map = new ConcurrentHashMap<>();

    /**
     * 定义切面
     */
    @Pointcut("@annotation(com.pyg.annotation.AnRateLimiter)")
    public void RateLimiterAspect() {
        System.out.println("注解类");
    }

    /**
     * 定义操作
     */
    @Around(value = "RateLimiterAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("进入限流器...");
        Object obj = null;
        //获取当前方法参数名称
        String methodName = joinPoint.getSignature().getName();
        //获取方法
        Method method = ((MethodSignature) (joinPoint.getSignature())).getMethod();
        //获取注解对象
        AnRateLimiter annotation = method.getAnnotation(AnRateLimiter.class);
        if (annotation != null) {
            double permitsPerSecond = annotation.permitsPerSecond();
            long timeout = annotation.timeout();
            TimeUnit timeUnit = annotation.timeUnit();
            String msg = annotation.msg();
            RateLimiter rateLimiter = null;
            if (!map.containsKey(methodName)) {
                // 创建令牌桶
                rateLimiter = RateLimiter.create(permitsPerSecond);
                map.put(methodName, rateLimiter);
            }
            rateLimiter = map.get(methodName);
            if (rateLimiter.tryAcquire(timeout, timeUnit)) {
                System.out.println("流量充足!");
                obj = joinPoint.proceed();
                return obj;
            } else {
                throw new RuntimeException(msg);
            }
        } else {
            throw new RuntimeException("未知错误，限流注解为null");
        }
    }

    private String responseResult(PygResult result) {
        return JSON.toJSONString(result);
    }

}
