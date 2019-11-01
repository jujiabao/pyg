package com.pyg.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @Title AnIpLimiter
 * @ProjectName pyg
 * @Description 单位时间内访问ip限制
 * @Author Hello.Ju
 * @Date 2019-11-01 11:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface AnReqIpLimiter {
    /**
     * 次数限制
     * @return
     */
    int count() default Integer.MAX_VALUE;

    /**
     * 时间段，单位为毫秒，默认值一分钟
     * @return
     */
    long time() default 60000;
}
