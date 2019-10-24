package com.pyg.cart.service;

import com.pyg.pojogroup.Cart;

import java.util.List;

/**
 * @Title CartService
 * @ProjectName pyg
 * @Description 购物车服务接口
 * @Author Hello.Ju
 * @Date 2019-10-23 22:27
 */
public interface CartService {

    /**
     * 添加商品到购物车
     * @param list 原购物车列表
     * @param itemId SKU的ID
     * @param num 数量
     * @return
     */
    List<Cart> add(List<Cart> list, Long itemId, Integer num);

    /**
     * 从Redis中获取购物车
     * @param userName
     * @return
     */
    List<Cart> findCartListFromRedis(String userName);

    /**
     * 保存购物车列表到Redis中
     * @param userName
     * @param cartList
     */
    void saveCartListToRedis(String userName, List<Cart> cartList);

    /**
     * 合并本地远程购物车
     * @param cartList1
     * @param cartList2
     * @return
     */
    List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2);
}
