package com.pyg.page.service;

/**
 * @Title ItemPageService
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-15 22:07
 */
public interface ItemPageService {

    /**
     * 生成商品详细页
     * @param goodsId
     * @return
     */
    boolean genItemHtml(Long goodsId);

    /**
     * 删除页面
     * @param goodsId
     * @return
     */
    boolean deleteHtml(Long goodsId);
}
