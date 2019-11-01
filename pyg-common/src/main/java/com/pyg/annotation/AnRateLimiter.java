package com.pyg.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Title AnRateLimiter
 * @ProjectName pyg
 * @Description 限流注解
 * @Author Hello.Ju
 * @Date 2019-10-31 16:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AnRateLimiter {
    /**
     * 每秒创建令牌个数，默认为10
     *
     * @return
     */
    double permitsPerSecond() default 10D;

    /**
     * 获取令牌超时时间
     *
     * @return
     */
    long timeout() default 0;

    /**
     * 超时时间单位
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 无法获取令牌返回提示信息 默认值可以自行修改
     *
     * @return
     */
    String msg() default "系统繁忙,请稍后再试.";
}
