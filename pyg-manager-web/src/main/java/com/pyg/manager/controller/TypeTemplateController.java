package com.pyg.manager.controller;
import java.util.List;

import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbTypeTemplate;
import com.pyg.manager.service.TypeTemplateService;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

	@Reference
	private TypeTemplateService typeTemplateService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbTypeTemplate> findAll(){			
		return typeTemplateService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return typeTemplateService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/add")
	public PygResult add(@RequestBody TbTypeTemplate typeTemplate){
		try {
			typeTemplateService.add(typeTemplate);
			return new PygResult(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody TbTypeTemplate typeTemplate){
		try {
			typeTemplateService.update(typeTemplate);
			return new PygResult(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbTypeTemplate findOne(Long id){
		return typeTemplateService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public PygResult delete(Long [] ids){
		try {
			typeTemplateService.delete(ids);
			return new PygResult(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbTypeTemplate typeTemplate, int page, int rows  ){
		return typeTemplateService.findPage(typeTemplate, page, rows);		
	}
	
}
