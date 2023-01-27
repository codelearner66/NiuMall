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
import com.hechi.niumall.vo.OrderListforSell;
import com.hechi.niumall.vo.orderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    GoodsService goodsService;
    @Autowired
    UserAddrService userAddrService;

    @Autowired
    OrderMapper orderMapper;

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

    /**
     * 分页获取用户所有订单
     *
     * @param userId 用户id
     * @param pages  页码
     * @return 分页数据
     */
    @Override
    public ResponseResult getOrderByUserId(Long userId, Integer pages) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        Page<Order> page1 = new Page<>(pages, 10);
        page(page1, queryWrapper);

        if (page1.getRecords() != null) {
            return ResponseResult.okResult(page1);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_USER_IS_NULL);
    }

    /**
     * 用户未支付订单
     *
     * @param userId 用户id
     * @param page   页码
     * @return 分页数据
     */
    @Override
    public ResponseResult getOrderByUserIdFornotPay(Long userId, int page) {
        return this.getResult(userId, page, SystemConstants.ORDER_NOT_PAY);
    }

    /**
     * 查询订单数据
     * @param userId 用户id
     * @param page   页码
     * @param status 订单状态
     * @return 分页数据
     */
    private ResponseResult getResult(Long userId, int page, int... status) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        log.info("收到了状态值{}",status);
        queryWrapper.eq(Order::getUserId, userId)
                .and(orderLambdaQueryWrapper -> {
                    for (int i : status) {
                        orderLambdaQueryWrapper.or().eq(Order ::getOrderStatus,i);
                    }
                });
        Page<Order> orderPage = new Page<>(page, 10);
        page(orderPage, queryWrapper);
        return ResponseResult.okResult(orderPage);
    }
    private ResponseResult getResult(int page, int... status) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        log.info("收到了状态值{}",status);
        queryWrapper.and(orderLambdaQueryWrapper -> {
                    for (int i : status) {
                        orderLambdaQueryWrapper.or().eq(Order ::getOrderStatus,i);
                    }
                });
        Page<Order> orderPage = new Page<>(page, 10);
        page(orderPage, queryWrapper);
        return ResponseResult.okResult(orderPage);
    }
    @Override
    public ResponseResult getOrderByUserIdwithStatus(Long userId, Integer page, int... status) {
        return this.getResult(userId, page, status);
    }

    @Override
    public ResponseResult getOrderWithStatus(Integer page, int... status) {
        return this.getResult(page, status);
    }

//    通过orderId 获取用户订单
    @Override
    public ResponseResult queryOrderByOrderId(String orderId) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order ::getOrderId,orderId);
        Page<Order> page1 = new Page<>(1,10);
        page(page1,queryWrapper);
        return ResponseResult.okResult(page1);
    }

    @Override
    public ResponseResult getOrderByUserIdForPayed(Long userId, int page) {
        return this.getResult(userId, page, SystemConstants.ORDER_PAID);
    }

    /**
     * 根据订单编号获取订单信息
     *
     * @param outTradeNo 订单编号
     * @return 订单信息
     */
    @Override
    public Order getOrderByOrderNo(String outTradeNo) {
        LambdaQueryWrapper<Order> lambda = new LambdaQueryWrapper<>();
        lambda.eq(Order::getOrderId, outTradeNo);
        return getOne(lambda);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        order.setPaymentType(goods.getPaymentType() == 1 ? SystemConstants.ALI_PAY : SystemConstants.BALANCE_PAY);
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
        goods1.setSalesCount(goods1.getSalesCount() + goods.getNum());
        goods1.setInventory(goods1.getInventory() - goods.getNum());
        Random random = new Random();
        goods1.setAccessCount(goods1.getAccessCount() + random.nextInt(10));
        goodsService.updateGoods(goods1);
        return ResponseResult.okResult(order);
    }

    /**
     * 更新订单信息
     *
     * @param order 订单信息
     * @return
     */
    @Override
    public ResponseResult updateOrder(Order order) {
        LambdaUpdateWrapper<Order> updateWrap = new LambdaUpdateWrapper<>();
        updateWrap.eq(Order::getOrderId, order.getOrderId());
        boolean update = update(order, updateWrap);
        return update ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.ORDER_IS_NULL);
    }

    @Override
    public ResponseResult shopped(Order order) {
        Order orderByOrderNo = this.getOrderByOrderNo(order.getOrderId());
        orderByOrderNo.setOrderStatus(SystemConstants.ORDER_SHIPPED);
        orderByOrderNo.setShippingName("顺丰快递");
        orderByOrderNo.setShippingCode(OrderNoUtils.getOrderNo());
        orderByOrderNo.setConsignTime(new Date());
        return this.updateOrder(orderByOrderNo);
    }

    @Override
    public List<OrderListforSell> getListforSelL() {
        List<OrderListforSell> listforSelL = orderMapper.getListforSelL();
        if (listforSelL.size() > 0) {
            for (OrderListforSell sell : listforSelL) {
                String orderContent = sell.getOrderContent();
                Goods goods = JSON.parseObject(orderContent, Goods.class);
                sell.setGoods(goods);
            }
        }
        return listforSelL;
    }

    @Override
    public ResponseResult getAllOrder(Integer pages) {
        return getResult(pages, 1,2,3,4,5,6,7,8,9,10);
    }

}