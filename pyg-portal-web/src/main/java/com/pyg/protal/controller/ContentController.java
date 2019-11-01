package com.pyg.protal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.annotation.AnRateLimiter;
import com.pyg.content.service.ContentService;
import com.pyg.pojo.TbContent;
import com.pyg.protal.ann.TestAnno;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @Title ContentController
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-09 20:08
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    @RequestMapping("/findByCategoryId")
    @AnRateLimiter(permitsPerSecond = 10d, msg = "系统繁忙，请稍后再试！！！！")
    public List<TbContent> findByCategoryId(Long id) {
        return contentService.findByCategoryId(id);
    }

}
