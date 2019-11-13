package com.pyg.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pyg.cart.service.CartService;
import com.pyg.pojogroup.Cart;
import com.pyg.utils.CookieUtil;
import com.pyg.utils.PygResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Title CartController
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-10-23 23:21
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    /**
     * 实现跨域请求
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping("/addGoods2CartList")
    @CrossOrigin(origins = "http://localhost:9105", allowCredentials = "true")//允许该服务的跨域请求
    public PygResult addGoods2CartList(Long itemId, Integer num) {
        //当前登录人账号
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录人："+name);
        try {
            List cartList = findCartList();
            cartList = cartService.add(cartList, itemId, num);
            if ("anonymousUser".equals(name)) {
                String cartListString = JSON.toJSONString(cartList);
                CookieUtil.setCookie(request, response, "cartList", cartListString, 3600 * 24, "utf-8");
                return new PygResult(true, "存入购物车成功（cookie）");
            } else {
                cartService.saveCartListToRedis(name, cartList);
                return new PygResult(true, "存入购物车成功（redis）");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false, "操作购物车失败");
        }
    }
    @RequestMapping("/findCartList")
    public List findCartList() {
        //当前登录人账号
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录人："+name);
        String value = CookieUtil.getCookieValue(request, "cartList", "utf-8");
        if (value == null || "".equals(value)) {
            value = "[]";
        }
        List<Cart> cartList = JSON.parseArray(value, Cart.class);
        if ("anonymousUser".equals(name)) {
            //未登录
            System.out.println("从cookie中读取购物车");
            return cartList;
        } else {
            //已登录
            List<Cart> cartList2 = cartService.findCartListFromRedis(name);
            if (cartList.size() > 0) {
                //合并购物车
                cartList2 = cartService.mergeCartList(cartList, cartList2);
                //合并后的购物车存入Redis
                cartService.saveCartListToRedis(name, cartList2);
                //清除本地cookie
                CookieUtil.deleteCookie(request, response, "cartList");
            }
            return cartList2;
        }
    }
}
