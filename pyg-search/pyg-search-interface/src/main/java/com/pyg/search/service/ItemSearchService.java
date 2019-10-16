package com.pyg.search.service;

import java.util.List;
import java.util.Map;

/**
 * @Title ItemSearchService
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-12 21:18
 */
public interface ItemSearchService {

    /**
     * 搜索关键字方法
     * @param searchMap
     * @return
     */
    Map search(Map searchMap);

    /**
     * 导入列表功能
     * @param list
     */
    void importList(List list);

    /**
     * 删除商品列表
     * @param goodsIds（spu）
     */
    void deleteByGoodsIds(List goodsIds);
}
