package com.hechi.niumall.controller.shopCart;

import com.hechi.niumall.entity.ShopCart;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.ShopCartService;
import com.hechi.niumall.vo.shopCartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shopcart")
public class ShopCartController {
    @Autowired
    ShopCartService shopCartService;

    @PostMapping("/add")
    public ResponseResult addShopCart(@RequestBody ShopCart cart){
        log.info("添加购物车");
       return shopCartService.addShopCart(cart);
    }
    @PostMapping("/updata")
    public ResponseResult updataShopCart(@RequestBody ShopCart cart) {
        log.info("更新购物车");
        return shopCartService.updataShopCart(cart);
    }
    @GetMapping("/delete/{id}")
    public ResponseResult deleteShopCart(@PathVariable String id) {
        log.info("删除购物车");
        ShopCart cart=new ShopCart();
        cart.setId(id);
      return   shopCartService.deleteShopCart(cart);
    }

    @GetMapping("/getShopCartByUserId")
    public ResponseResult getShopCartByUserId(Integer pages, Integer size){
        log.info("获取购物车信息");
      return   shopCartService.getShopCartByUserId(pages,size);
    }

    /**
     * 批量结算
     * @return
     */
    @PostMapping("/settleAccounts")
    public ResponseResult  settleAccounts(@RequestBody shopCartVo list){
      return   shopCartService.settleAccounts(list);
    }
}
