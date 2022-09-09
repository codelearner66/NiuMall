package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.Goods;
import com.hechi.niumall.entity.ShopCart;
import com.hechi.niumall.entity.UserAddr;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.mapper.ShopCartMapper;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.BalancePayService;
import com.hechi.niumall.service.GoodsService;
import com.hechi.niumall.service.ShopCartService;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.vo.orderVo;
import com.hechi.niumall.vo.shopCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * niuMall商城-购物车(ShopCart)表服务实现类
 *
 * @author ccx
 * @since 2022-08-23 22:48:12
 */
@Service("shopCartService")
public class ShopCartServiceImpl extends ServiceImpl<ShopCartMapper, ShopCart> implements ShopCartService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    BalancePayService balancePayService;
    @Override
    public ResponseResult addShopCart(ShopCart shopCart) {
//        查询购物车 如果已经存在 就修改数量和总价 如果不存在进新建购物车
        LambdaQueryWrapper<ShopCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopCart ::getGoodsId,shopCart.getGoodsId())
                .eq(ShopCart ::getUserId,Math.toIntExact(SecurityUtils.getUserId()));
        ShopCart one = getOne(wrapper);
        if (one!=null){
            one.setStock(one.getStock()+ shopCart.getStock());
            one.setSum(String.valueOf(one.getStock()*Double.parseDouble(one.getPrice())));
            boolean b = updateById(one);
            return b?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.SHOPCART_ERROE);
        }
        Goods byId = goodsService.getById(shopCart.getGoodsId());
        if (byId == null) {
            throw new RuntimeException(AppHttpCodeEnum.GOODS_IS_NOT_EXTST.getMsg());
        }
//        设置用户id
        shopCart.setUserId(Math.toIntExact(SecurityUtils.getUserId()));
        shopCart.setTitle(byId.getTitle());
        shopCart.setImages(byId.getHomeRecommendedImages());
        double v = Double.parseDouble(byId.getPrice());
        shopCart.setSum(String.valueOf(shopCart.getStock()*v));
        shopCart.setPrice(byId.getPrice());
        shopCart.setUpdateTime(new Date());
        baseMapper.insert(shopCart);
        return ResponseResult.okResult();
    }

    @Override
    @CacheEvict(value = "ShopCart",key="'ShopCart:'+#shopCart.userId")
    public ResponseResult updataShopCart(ShopCart shopCart) {
        boolean b = updateById(shopCart);
        return b?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.SHOPCART_ERROE);
    }

    @Override
    @CacheEvict(value = "ShopCart",key="'ShopCart:'+#shopCart.userId")
    public ResponseResult deleteShopCart(ShopCart shopCart) {
        boolean b = removeById(shopCart.getId());
        return b?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.SHOPCART_DELETE_ERROE);
    }

    @Override
    @Cacheable(value = "ShopCart",key = "'ShopCart:'+#userId")
    public ResponseResult getShopCartByUserId(Integer userId, int pages, int size) {
        LambdaQueryWrapper<ShopCart> wrap=new LambdaQueryWrapper<>();
        wrap.eq(ShopCart ::getUserId,userId);
        Page<ShopCart> page1 = new Page<>(pages, size);
        page(page1,wrap);
        return ResponseResult.okResult(page1);
    }


    @Override
    public ResponseResult getShopCartByUserId(int pages, int size) {
        return this.getShopCartByUserId(Math.toIntExact(SecurityUtils.getUserId()),pages,size);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult settleAccounts(shopCartVo list) {
        List<ShopCart> list1 = list.getList();
        UserAddr userAddr = list.getUserAddr();
        String s=null;
        for (ShopCart shopCart : list1) {
            orderVo orderVo = new orderVo();
            orderVo.setGoodsId(Long.valueOf(shopCart.getGoodsId()));
            orderVo.setSum(Integer.parseInt(shopCart.getSum().substring(0,shopCart.getSum().length()-3)));
            orderVo.setNum(shopCart.getStock());
            orderVo.setUserAddr(userAddr);
            orderVo.setPaymentType(0);
            balancePayService.tradeCreate(orderVo);
            this.deleteShopCart(shopCart);
        }
        return ResponseResult.okResult(s);
    }
}
