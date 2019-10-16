package com.pyg.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.pojo.TbItem;
import com.pyg.search.service.ItemSearchService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title ItemSearchServiceImpl
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-12 21:21
 */
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map search(Map searchMap) {

        /*Map map = new HashedMap();

        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        ScoredPage<TbItem> items = solrTemplate.queryForPage(query, TbItem.class);

        map.put("rows", items.getContent());

        return map;*/

        Map map = new HashedMap();
        //去掉关键字空格
        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords", keywords.replace(" ", ""));

        //查询列表
        map.putAll(searchList(searchMap));

        //查询商品分类列表
        List<String> list = searchCategoryList(searchMap);
        map.put("categoryList", list);

        //查询品牌和规格列表
        String category = (String) searchMap.get("category");
        if (!"".equals(category)) {
            map.putAll(searchBrandAndSpecList(category));
        } else {
            if (list.size() > 0) {
                map.putAll(searchBrandAndSpecList(list.get(0)));
            }
        }

        return map;
    }

    @Override
    public void importList(List list) {

        solrTemplate.saveBeans(list);
        solrTemplate.commit();

    }

    @Override
    public void deleteByGoodsIds(List goodsIds) {
        SolrDataQuery query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_goodsid");
        criteria.in(goodsIds);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    //查询列表
    private Map searchList(Map searchMap) {
        Map map = new HashedMap();
        //高亮显示
        HighlightQuery query = new SimpleHighlightQuery();
        HighlightOptions options = new HighlightOptions().addField("item_title");
        options.setSimplePrefix("<em style='color:red'>");
        options.setSimplePostfix("</em>");
        query.setHighlightOptions(options);

        //1.1关键字搜索
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //1.2按照商品分类过滤
        if (!StringUtils.isEmpty((String) searchMap.get("category"))) {
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //1.3按照品牌分类过滤
        if (!StringUtils.isEmpty((String) searchMap.get("brand"))) {
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        //1.4按照规格过滤
        if (searchMap.get("spec") != null) {
            Map<String, String> specMap = (Map<String, String>) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //1.5按照价格过滤
        if (!"".equals(searchMap.get("price"))) {
            String[] prices = ((String) searchMap.get("price")).split("-");
            if (!prices[0].equals(0)) {//如果最低价格不等于0
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(prices[0]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            if (!prices[1].equals("*")) {//最高价格不等于*
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(prices[1]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //1.6分页
        Integer pageNo = (Integer) searchMap.get("pageNo");
        if (pageNo == null) {
            pageNo = 1;
        }
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (pageSize == null) {
            pageSize = 20;
        }
        query.setOffset((pageNo - 1) * pageSize);
        query.setRows(pageSize);

        //1.7排序
        String sortValue = (String) searchMap.get("sort");
        String sortField = (String) searchMap.get("sortField");

        if (sortValue != null && !"".equals(sortValue)) {
            if ("ASC".equals(sortValue)) {
                Sort sort = new Sort(Sort.Direction.ASC, "item_"+sortField);
                query.addSort(sort);
            }
            if ("DESC".equals(sortValue)) {
                Sort sort = new Sort(Sort.Direction.DESC, "item_"+sortField);
                query.addSort(sort);
            }
        }

        HighlightPage<TbItem> items = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //高亮入口集合
        List<HighlightEntry<TbItem>> list = items.getHighlighted();

        for (HighlightEntry<TbItem> entry : list) {
            List<HighlightEntry.Highlight> highlights = entry.getHighlights();
            /*for (HighlightEntry.Highlight highlight : highlights) {
                List<String> snipplets = highlight.getSnipplets();
                System.out.println(snipplets);
            }*/
            if (highlights.size() > 0) {
                TbItem entity = entry.getEntity();
                entity.setTitle(highlights.get(0).getSnipplets().get(0));
            }

        }
        map.put("rows", items.getContent());
        map.put("totalPages", items.getTotalPages());//总页数
        map.put("total", items.getTotalElements());//总记录数
        return map;
    }

    //查询商品分类分组列表
    private List<String> searchCategoryList(Map searchMap) {

        Query query = new SimpleQuery("*:*");
        //where查询条件
        Criteria c = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(c);

        //group by 分组查询
        GroupOptions options = new GroupOptions();
        options.addGroupByField("item_category");
        query.setGroupOptions(options);
        //设置分组页选项
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        //获取分组结果对象
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        //获取分组入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        //获取分组入口集合
        List<GroupEntry<TbItem>> entryList = groupEntries.getContent();

        List<String> list = new ArrayList<>();

        for (GroupEntry<TbItem> tbItemGroupEntry : entryList) {
            list.add(tbItemGroupEntry.getGroupValue());
        }
        return list;
    }

    /**
     * 根据商品分类名称查询品牌列表和规格列表
     *
     * @param categoryName
     * @return
     */
    private Map searchBrandAndSpecList(String categoryName) {
        Map map = new HashMap();
        Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(categoryName);

        if (templateId != null) {
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
            map.put("brandList", brandList);

            List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
            map.put("specList", specList);
        }
        return map;
    }
}
