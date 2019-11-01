package com.pyg.aspect;

import com.pyg.annotation.AnReqIpLimiter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Title ReqIpLimter
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-11-01 11:32
 */
@Aspect
@Component
public class ReqIpLimter {
    private static final Logger logger = LoggerFactory.getLogger(ReqIpLimter.class);
    /**
     * 自定义ip存储规则key:ip,value:long时间
     */
    private final Map<String, List<Long>> map = new ConcurrentHashMap<>();

    @Before("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(reqIpLimiter)")
    public void reqIpLimit(final JoinPoint joinPoint, AnReqIpLimiter reqIpLimiter) {
        long reqMillis = System.currentTimeMillis();

        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                request = (HttpServletRequest) args[i];
                break;
            }
        }
        if (request == null) {
            throw new RuntimeException("使用ip控制，在方法中必须传递request对象");
        }
        String ip = getIpAddr(request);
        String url = request.getRequestURL().toString();
        List<Long> list = map.get(ip);
        List<Long> temp = new ArrayList<>();
        if (list != null) {
            //是否在规定时间内，且请求次数过多
            if ((list.get(0) + reqIpLimiter.time() > reqMillis) && list.get(1) >= reqIpLimiter.count()) {
                logger.info("用户IP[{}]访问地址[{}]规定时间内[{}]超过了限定的次数[{}]，请求拒绝", ip, url, reqIpLimiter.time(), reqIpLimiter.count());
                throw new RuntimeException("请求次数超限，交易拒绝");
            } else if (list.get(0) + reqIpLimiter.time() <= reqMillis) {
                temp.add(reqMillis);
                temp.add(1L);
                map.put(ip, temp);
            } else {
                temp.add(list.get(0));
                temp.add(list.get(1) + 1L);
                map.put(ip, temp);
            }
        } else {
            temp.add(reqMillis);
            temp.add(1L);
            map.put(ip, temp);
        }
        logger.info("用户IP[{}]访问地址[{}]规定时间内[{}]请求了[{}]次，请求继续",ip, url, reqIpLimiter.time(), map.get(ip).get(1));
        temp = null;
    }


    /**
     * 客户端真实IP地址
     */
    private static String getIpAddr(HttpServletRequest request) {
        String ip = "";
        if (request != null) {
            ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }
}
