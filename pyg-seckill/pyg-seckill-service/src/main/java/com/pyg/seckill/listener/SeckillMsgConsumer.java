package com.pyg.seckill.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.IOException;
import java.util.Date;

/**
 * @Title SeckillMsgConsumer
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-11-01 10:28
 */
public class SeckillMsgConsumer implements MessageListener {
    @Override
    public void onMessage(Message message) {
        long begin = System.currentTimeMillis();
        System.out.println("接收到消息..."+begin);
        try {
            JsonNode node = new ObjectMapper().readTree(message.getBody());
            System.out.println("处理商品："+node.get("goodsId").asText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("处理结束:"+(System.currentTimeMillis()-begin));
    }
}
