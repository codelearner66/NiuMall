package com.hechi.niumall.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.constants.SystemConstants;
import com.hechi.niumall.entity.Goods;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.mapper.OrderMapper;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.GoodsService;
import com.hechi.niumall.service.OrderService;
import com.hechi.niumall.service.UserAddrService;
import com.hechi.niumall.utils.OrderNoUtils;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.vo.orderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * niumall商城-订单(Order)表服务实现类
 *
 * @author ccx
 * @since 2022-08-09 21:59:54
 */

@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserAddrService userAddrService;

    @Override
    public ResponseResult getOrderById(Long id) {
        //     id 非空
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getId, id);
        Order one = getOne(queryWrapper);
        if (one == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_IS_NULL);
        }
        return ResponseResult.okResult(one);
    }

    @Override
    public ResponseResult getOrderByUserId(Long userId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        List<Order> list = list(queryWrapper);
        if (list.size() > 0) {
            return ResponseResult.okResult(list);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_USER_IS_NULL);
    }

    @Override
    @Cacheable(value = "orders",key = "'userId:'+#userId+'p'+#page")
    public ResponseResult getOrderByUserIdFornotPay(Long userId, int page) {
        return this.getResult(userId, page, SystemConstants.ORDER_NOT_PAY);
    }

    /**
     * 查询订单数据
     * @param userId 用户id
     * @param page  页码
     * @param status 订单状态
     * @return 分页数据
     */
    private ResponseResult getResult(Long userId, int page,int status){
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId)
                .eq(Order ::getOrderStatus,status);
        Page<Order> orderPage = new Page<>(page,10);
        page(orderPage, queryWrapper);
        return ResponseResult.okResult(orderPage);
    }

    @Override
    public ResponseResult getOrderByUserIdForPayed(Long userId, int page) {
        return this.getResult(userId, page, SystemConstants.ORDER_PAID);
    }

    @Override
    public Order getOrderByOrderNo(String outTradeNo) {
        LambdaQueryWrapper<Order> lambda=new LambdaQueryWrapper<>();
       lambda.eq(Order::getOrderId, outTradeNo);
        return getOne(lambda);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult createOrder(orderVo goods) {
//        检查已有未完成订单就直接返回给用户
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, SecurityUtils.getUserId())
                .eq(Order::getGoodsId, Integer.parseInt(String.valueOf(goods.getGoodsId())))
                .eq(Order::getOrderStatus, 1);
        Order one = getOne(queryWrapper);
        if (one != null) {
            return ResponseResult.okResult(one);
        }
//新建订单
        ResponseResult goodsDetilsById = goodsService.getGoodsDetilsById(Integer.valueOf(String.valueOf(goods.getGoodsId())));
        Goods goods1 = (Goods) goodsDetilsById.getData();
        Order order = new Order();
        //        设置用户id
        order.setUserId(SecurityUtils.getUserId());
        //设置用户地址id
        order.setAddrId(Long.valueOf(goods.getUserAddr().getId()));
        // 设置订单编号
        order.setOrderId(OrderNoUtils.getOrderNo());
        // 设置商品id
        order.setGoodsId(Long.valueOf(String.valueOf(goods.getGoodsId())));
        //设置商品详细信息
        String s = JSON.toJSONString(goods1);
        order.setOrderContent(s);
        // 设置实付金额
        order.setPayment(goods.getSum() + ".00");
        //设置支付类型  动态指定
        order.setPaymentType(goods.getPaymentType()==1?SystemConstants.ALI_PAY:SystemConstants.BALANCE_PAY);
        //设置订单状态
        order.setOrderStatus(SystemConstants.ORDER_NOT_PAY);
        //设置邮费
        order.setPostFee("0.00");
        Date date = new Date();
        order.setCreateTime(date);
//新建订单完成后 更新商品信息 库存减去相应的数量 销量增加相应的数量
        //保存订单
        save(order);
//  更新商品信息
        goods1.setSalesCount(goods1.getSalesCount()+goods.getNum());
        goods1.setInventory(goods1.getInventory()-goods.getNum());
        Random random=new Random();
        goods1.setAccessCount(goods1.getAccessCount()+random.nextInt(10));
        goodsService.updateGoods(goods1);
        return ResponseResult.okResult(order);
    }

    @Override
    public ResponseResult updateOrder(Order order) {
        LambdaQueryWrapper<Order> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Order::getOrderId,order.getOrderId());
        Order one = getOne(lambdaQueryWrapper);
        if (one!=null){
            LambdaUpdateWrapper<Order> updateWrap=new LambdaUpdateWrapper<>();
            updateWrap.eq(Order ::getOrderId,order.getOrderId());
            update(order,updateWrap);
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_IS_NULL);
    }
}