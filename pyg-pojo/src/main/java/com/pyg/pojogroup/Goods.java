package com.pyg.pojogroup;

import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Title Goods
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-08-12 22:18
 */
public class Goods implements Serializable {
    private TbGoods goods;//商品SPU基本信息
    private TbGoodsDesc goodsDesc;//商品SPU扩展信息
    private List<TbItem> itemList;//SKU列表

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods(TbGoods goods) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(TbGoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "goods=" + goods +
                ", goodsDesc=" + goodsDesc +
                ", itemList=" + itemList +
                '}';
    }
}
