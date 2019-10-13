package com.pyg.manager.service.impl;

import java.util.List;

import com.pyg.pojo.TbItemCatExample;
import com.pyg.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbItemCatMapper;
import com.pyg.pojo.TbItemCat;
import com.pyg.manager.service.ItemCatService;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service(timeout = 8000)
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbItemCat> findAll() {
        return itemCatMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbItemCat itemCat) {
        itemCatMapper.insert(itemCat);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbItemCat itemCat) {
        itemCatMapper.updateByPrimaryKey(itemCat);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            itemCatMapper.deleteByPrimaryKey(id);

            TbItemCatExample example = new TbItemCatExample();
            TbItemCatExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(example);


            TbItemCatExample example2 = new TbItemCatExample();
            TbItemCatExample.Criteria criteria2 = example2.createCriteria();
            criteria2.andParentIdEqualTo(id);
            itemCatMapper.deleteByExample(example2);

            for (TbItemCat itemCat : tbItemCats) {
                TbItemCatExample example1 = new TbItemCatExample();
                TbItemCatExample.Criteria criteria1 = example1.createCriteria();
                criteria1.andParentIdEqualTo(itemCat.getId());
                itemCatMapper.deleteByExample(example1);
            }
        }
    }


    @Override
    public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();

        if (itemCat != null) {
            if (itemCat.getName() != null && itemCat.getName().length() > 0) {
                criteria.andNameLike("%" + itemCat.getName() + "%");
            }

        }

        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<TbItemCat> findByParentId(Long parentId) {

        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        //将模板id放入缓存
        List<TbItemCat> itemCats = findAll();
        for (TbItemCat itemCat : itemCats) {
            redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
        }
        System.out.println("将模板ID放入缓存。");

        return itemCatMapper.selectByExample(example);
    }

}
