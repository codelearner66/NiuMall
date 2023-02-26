package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.vo.OrderListforSell;
import com.hechi.niumall.vo.orderVo;

import java.util.List;


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
    ResponseResult getOrderByUserId(Long userId,Integer pages);

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
    ResponseResult getOrderByUserIdwithStatus(Long userId,Integer page,int... status);

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

    /**
     * 获取分类销量前十
     * @return
     */
    public List<OrderListforSell> getListforSelL();


    /**
     * 管理员获取所有用户订单
     * @param pages 分页页码
     * @return 用户订单信息
     */
    ResponseResult getAllOrder(Integer pages);

    public ResponseResult getOrderWithStatus(Integer page, int... status);

    ResponseResult queryOrderByOrderId(String orderId);

    ResponseResult shopped(Order order);

   Order getOrderByUserIdandGoodsId(Long userid,Long GoodsId);
}

