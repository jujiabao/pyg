package com.pyg.manager.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.pyg.pojo.TbItem;
import com.pyg.pojogroup.Goods;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbGoods;
import com.pyg.manager.service.GoodsService;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination queueSolrDestination;

    @Autowired
    private Destination queueSolrDeleteDestination;

    @Autowired
    private Destination topicPageDestination;

    @Autowired
    private Destination topicPageDeleteDestination;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return goodsService.findPage(page, rows);
    }


    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public PygResult update(@RequestBody Goods goods) {
        try {
            goodsService.update(goods);
            return new PygResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public PygResult delete(Long[] ids) {
        try {
            goodsService.delete(ids);
            //从solr库删除
            jmsTemplate.send(queueSolrDeleteDestination, session -> session.createObjectMessage(ids));
            //删除page页面
            jmsTemplate.send(topicPageDeleteDestination, session -> session.createObjectMessage(ids));
            return new PygResult(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int page, int rows) {
        return goodsService.findPage(goods, page, rows);
    }

    /**
     * 修改状态
     *
     * @param ids
     * @param status
     * @return
     */
    @RequestMapping("/updateStatus")
    public PygResult updateStatus(Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            if ("1".equals(status)) {
                //需要导入solr的数据
                List<TbItem> list = goodsService.findItemListByGoodsIdListAndStatus(ids, status);
                String jsonString = JSON.toJSONString(list);
                //往消息队列扔信息
                jmsTemplate.send(queueSolrDestination, session -> session.createTextMessage(jsonString));

                //生成商品详细页
                for (Long goodsId : ids) {
                    jmsTemplate.send(topicPageDestination, session -> session.createObjectMessage(goodsId));
                }
            }
            return new PygResult(true, "成功");
        } catch (Exception e) {
            return new PygResult(false, "失败");
        }
    }

	/*@RequestMapping("/gen")
	public void genHtml(Long goodsId) {
		itemPageService.genItemHtml(goodsId);
	}*/

}
