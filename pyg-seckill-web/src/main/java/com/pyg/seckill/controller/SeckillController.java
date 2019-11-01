package com.pyg.seckill.controller;

import com.pyg.annotation.AnRateLimiter;
import com.pyg.annotation.AnReqIpLimiter;
import com.pyg.utils.PygResult;
import com.pyg.utils.SeckillMsg;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title SeckillController
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-11-01 10:32
 */
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    private static final String QUEUE_KEY = "pyg_seckill_queue_key";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/execution/{goodsId}/{userId}/{md5}",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @AnReqIpLimiter(count = 2, time = 3000)
    @AnRateLimiter(permitsPerSecond = 10, msg = "系统繁忙")
    public PygResult execute(HttpServletRequest request,
                             @PathVariable("goodsId") Long goodsId,
                             @PathVariable("userId") Long userId,
                             @PathVariable("md5") String md5) {
        SeckillMsg msg = new SeckillMsg();
        msg.setGoodsId(1234L);
        msg.setUserId(1111L);

        rabbitTemplate.convertAndSend(QUEUE_KEY, msg);
        //清除内存
        msg = null;

        return new PygResult(true, "排队中");
    }
}
