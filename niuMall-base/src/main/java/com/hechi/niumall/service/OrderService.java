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
    /**
     *  通过id获取订单
      */

    ResponseResult getOrderById(Long id);

    /**
     *  通过 用户id 获取订单
      */

    ResponseResult getOrderByUserId(Long userId);

    /**
     * 分页查询未支付订单
     * @param userId 用户id
     * @param page 页码
     * @return 分页数据
     */
    ResponseResult getOrderByUserIdFornotPay(Long userId,int page);

    /**
     * 分页查询已支付订单
     * @param userId 用户 id
     * @param page 分页
     * @return  分页数据
     */
    ResponseResult getOrderByUserIdForPayed(Long userId,int page);

    /**
     * 查询用户订单
     * @param userId 用户id
     * @param page 页码
     * @param status 订单状态
     * @return
     */
    ResponseResult getOrderByUserIdwithStatus(Long userId,int page,Integer status);

//    ResponseResult getOrderByUserIdForPayed(Long userId,int page);
    /**
     * 生成订单
      */

    ResponseResult createOrder(orderVo goods);

    /**
     * 更新订单
      */

    ResponseResult updateOrder(Order order);

    Order getOrderByOrderNo(String outTradeNo);
}

