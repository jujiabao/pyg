package com.pyg.manager.controller;
import java.util.Arrays;
import java.util.List;

import com.pyg.page.service.ItemPageService;
import com.pyg.pojo.TbItem;
import com.pyg.pojogroup.Goods;
import com.pyg.search.service.ItemSearchService;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbGoods;
import com.pyg.manager.service.GoodsService;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	@Reference(timeout = 7000)
	private ItemSearchService itemSearchService;

	@Reference
	private ItemPageService itemPageService;
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int rows){
		return goodsService.findPage(page, rows);
	}

	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public PygResult update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
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
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public PygResult delete(Long [] ids){
		try {
			goodsService.delete(ids);
			//从solr库删除
			itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
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
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}

	/**
	 * 修改状态
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping("/updateStatus")
	public PygResult updateStatus(Long[] ids, String status) {
		try {
			goodsService.updateStatus(ids, status);
			if ("1".equals(status)) {
				//需要导入solr的数据
				List<TbItem> list = goodsService.findItemListByGoodsIdListAndStatus(ids, status);
				itemSearchService.importList(list);
				//生成商品详细页
				for (Long goodsId : ids) {
					itemPageService.genItemHtml(goodsId);
				}
			}
			return new PygResult(true, "成功");
		} catch (Exception e) {
			return new PygResult(false, "失败");
		}
	}

	/*@RequestMapping("/gen")
	public void genHtml(Long goodsId) {
		itemPageService.genItemHtml(goodsId);
	}*/
	
}
