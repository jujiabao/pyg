package com.pyg.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.BrandService;
import com.pyg.pojo.TbBrand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title BrandController
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-07-15 23:09
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference//注入远程zookeeper对象
    private BrandService brandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

}
