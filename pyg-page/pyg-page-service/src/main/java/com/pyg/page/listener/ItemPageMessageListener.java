package com.pyg.page.listener;

import com.pyg.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @Title ItemPageMessageListener
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-18 21:10
 */
@Component
public class ItemPageMessageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long id = (Long) objectMessage.getObject();
            System.out.println("生成page页服务接收到消息："+id);
            itemPageService.genItemHtml(id);
            System.out.println("生成商品页面成功...");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
