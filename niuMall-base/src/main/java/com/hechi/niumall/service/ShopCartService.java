package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.ShopCart;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.vo.shopCartVo;


/**
 * niuMall商城-购物车(ShopCart)表服务接口
 *
 * @author ccx
 * @since 2022-08-23 22:48:12
 */
public interface ShopCartService extends IService<ShopCart> {
// 添加购物车
          ResponseResult addShopCart(ShopCart shopCart);
          ResponseResult updataShopCart(ShopCart shopCart);
// 删除购物车
    ResponseResult deleteShopCart(ShopCart shopCart);

    /**
     * 通过用户id查询购物车
     * @param userId 用户id
     * @param pages  分页数
     * @param size   页面大小
     * @return
     */
    ResponseResult getShopCartByUserId(Integer userId,int pages,int size);
    ResponseResult getShopCartByUserId(int pages,int size);



// 购物车结算
 ResponseResult settleAccounts(shopCartVo list);

}

