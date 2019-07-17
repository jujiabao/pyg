package com.pyg.manager.service;

import com.pyg.pojo.TbBrand;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;

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
    List<TbBrand> findAll();

    //分页展示
    PageResult findPage(Integer page, Integer rows);

    //添加
    PygResult add(TbBrand tbBrand);

    //根据id查询品牌数据
    TbBrand findOne(Long id);

    PygResult update(TbBrand tbBrand);

    /**
     * 带条件查询
     * @param tbBrand
     * @param page
     * @param rows
     * @return
     */
    PageResult findBrandByPage(TbBrand tbBrand, Integer page, Integer rows);
}
