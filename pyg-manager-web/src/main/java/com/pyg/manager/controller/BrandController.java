package com.pyg.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.BrandService;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Reference(timeout = 100000)//注入远程zookeeper对象
    private BrandService brandService;

    /**
     * 全量查询品牌信息
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    /**
     * 分页查询品牌信息
     * @param page 起始页码
     * @param rows 查询笔数
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(Integer page, Integer rows) {
        return brandService.findPage(page, rows);
    }

    /**
     * 添加品牌数据
     * @param tbBrand
     * @return
     */
    @RequestMapping("/add")
    public PygResult add(@RequestBody TbBrand tbBrand) {
        return brandService.add(tbBrand);
    }

    /**
     * 根据id查询品牌参数
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbBrand add(Long id) {
        return brandService.findOne(id);
    }

    /**
     * 更新品牌信息
     * @param tbBrand
     * @return
     */
    @RequestMapping("/update")
    public PygResult update(@RequestBody TbBrand tbBrand) {
        return brandService.update(tbBrand);
    }

    /**
     * 条件查询品牌信息
     * @param tbBrand
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbBrand tbBrand,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer rows) {
        return brandService.findBrandByPage(tbBrand, page, rows);
    }
}
