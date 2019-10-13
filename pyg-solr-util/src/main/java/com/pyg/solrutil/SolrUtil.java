package com.pyg.solrutil;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Title 批量导入solr数据
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-11 22:27
 */
@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    public void importItemData() {
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");//审核通过
        List<TbItem> items = tbItemMapper.selectByExample(example);
        System.out.println("---商品列表---");
        for (TbItem item : items) {
            System.out.println(item.getId() + " " + item.getTitle() + " " + item.getPrice());
            Map map = JSON.parseObject(item.getSpec(), Map.class);//提取规格字符串
            item.setSpecMap(map);
            System.out.println(item.getSpecMap());
        }
        System.out.println("---结束---");
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil util = (SolrUtil) applicationContext.getBean("solrUtil");
        util.importItemData();
    }
}
