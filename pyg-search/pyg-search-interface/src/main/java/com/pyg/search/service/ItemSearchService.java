package com.pyg.search.service;

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
}
