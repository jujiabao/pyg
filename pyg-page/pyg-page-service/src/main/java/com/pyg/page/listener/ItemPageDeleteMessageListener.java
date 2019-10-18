package com.pyg.page.listener;

import com.pyg.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @Title ItemPageDeleteMessageListener
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-18 21:13
 */
@Component
public class ItemPageDeleteMessageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] goodsIds = (Long[]) objectMessage.getObject();
            System.out.println("接收到删除page页面消息："+goodsIds);
            for (Long goodsId : goodsIds) {
                itemPageService.deleteHtml(goodsId);
            }
            System.out.println("删除page页面成功...");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
