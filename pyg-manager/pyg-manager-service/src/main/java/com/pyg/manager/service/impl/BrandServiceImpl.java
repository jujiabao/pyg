package com.pyg.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pyg.manager.service.BrandService;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbBrandExample;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
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

    @Override
    public PageResult findPage(Integer page, Integer rows) {
        TbBrandExample example = new TbBrandExample();
        //使用插件设置分页
        PageHelper.startPage(page, rows);
        //查询品牌数据
        List<TbBrand> list = brandMapper.selectByExample(example);
        System.out.println("分页数据="+list);
        //创建pageinfo对象，获取分页数据
        PageInfo<TbBrand> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), list);
    }

    @Override
    public PygResult add(TbBrand tbBrand) {
        try {
            brandMapper.insertSelective(tbBrand);
            return new PygResult(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "保存失败");
        }
    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public PygResult update(TbBrand tbBrand) {
        try {
            brandMapper.updateByPrimaryKey(tbBrand);
            return new PygResult(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "修改失败");
        }
    }

    @Override
    public PageResult findBrandByPage(TbBrand tbBrand, Integer page, Integer rows) {
        TbBrandExample tbBrandExample = new TbBrandExample();
        //创建Criteria对象
        TbBrandExample.Criteria criteria = tbBrandExample.createCriteria();
        //设置参数
        if (tbBrand.getName() != null && !"".equals(tbBrand.getName())) {
            //模糊查询
            criteria.andNameLike("%"+tbBrand.getName()+"%");
        }
        if (tbBrand.getFirstChar() != null && !"".equals(tbBrand.getFirstChar())) {
            //模糊查询
            criteria.andFirstCharLike("%"+tbBrand.getFirstChar()+"%");
        }
        //设置分页
        PageHelper.startPage(page, rows);
        List<TbBrand> list = brandMapper.selectByExample(tbBrandExample);
        PageInfo<TbBrand> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(), list);
    }
}
