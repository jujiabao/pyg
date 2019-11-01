package com.pyg.protal.asp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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
public class MyAspect {
    public MyAspect() {
        System.out.println("aspect实例化");
    }

    @Pointcut("@annotation(com.pyg.protal.ann.TestAnno)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("打印日志一下....");
        Object obj = joinPoint.proceed();
        return obj;
    }
}
