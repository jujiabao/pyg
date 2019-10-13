package com.pyg.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Title ItemSearchController
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-12 22:14
 */
@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search")
    public Map search(@RequestBody Map searchMap){
        return itemSearchService.search(searchMap);
    }
}
