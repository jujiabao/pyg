package com.pyg.search.jms.listener;

import com.pyg.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Arrays;

/**
 * @Title ItemDeleteMessageListener
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-17 22:47
 */
@Component("itemDeleteMessageListener")
public class ItemDeleteMessageListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Long[] ids = (Long[]) objectMessage.getObject();
            System.out.println("监听到消息..."+ids);
            itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
            System.out.println("执行删除索引操作...");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
