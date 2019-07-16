package com.pyg.manager.service;

import com.pyg.pojo.TbBrand;

import java.util.List;

/**
 * @Title BrandService
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-07-15 23:04
 */
public interface BrandService {
    //查找所有的品牌
    public List<TbBrand> findAll();
}
