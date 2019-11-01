package com.pyg.protal.asp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Title MyAspect
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-31 21:35
 */
@Aspect
@Component
public class MyAspect2 {
    public MyAspect2() {
        System.out.println("aspect2实例化");
    }

    @Pointcut("execution(* com.pyg.protal.controller.ContentController.*(..))")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("MyAspect2...打印日志一下....");
        Object obj = joinPoint.proceed();
        return obj;
    }
}
