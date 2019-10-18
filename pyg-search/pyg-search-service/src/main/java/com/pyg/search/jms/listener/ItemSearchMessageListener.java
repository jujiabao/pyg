package com.pyg.search.jms.listener;

import com.alibaba.fastjson.JSON;
import com.pyg.pojo.TbItem;
import com.pyg.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

/**
 * @Title ItemSearchMessageListener
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-17 22:18
 */
@Component("itemSearchMessageListener")
public class ItemSearchMessageListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            System.out.println("监听到消息："+text);
            List<TbItem> itemList = JSON.parseArray(text, TbItem.class);
            itemSearchService.importList(itemList);
            System.out.println("导入solr索引库...");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
