package com.pyg.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.manager.service.BrandService;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbBrandExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Title BrandServiceImpl
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-07-15 23:06
 */
@Service//发布zookeeper服务
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        //查询
        TbBrandExample example = new TbBrandExample();
        List<TbBrand> list = brandMapper.selectByExample(example);
        return list;
    }
}
