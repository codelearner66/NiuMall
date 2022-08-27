package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.vo.orderVo;


/**
 * niumall商城-订单(Order)表服务接口
 *
 * @author ccx
 * @since 2022-08-09 21:59:54
 */
public interface OrderService extends IService<Order> {
    //    通过id获取订单
    ResponseResult getOrderById(Long id);

    //   通过 用户id 获取订单
    ResponseResult getOrderByUserId(Long userId);

    //    生成订单
    ResponseResult createOrder(orderVo goods);

    //    更新订单
    ResponseResult updateOrder(Order order);

    Order getOrderByOrderNo(String outTradeNo);
}

