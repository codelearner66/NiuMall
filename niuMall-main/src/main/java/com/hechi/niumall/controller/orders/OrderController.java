package com.hechi.niumall.controller.orders;

import com.hechi.niumall.constants.SystemConstants;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.entity.RefundInfo;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.OrderService;
import com.hechi.niumall.service.RefundInfoService;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.vo.OrderListforSell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    RefundInfoService refundInfoService;


    /**
     * 用户查询未支付订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrder/{page}")
    public ResponseResult getUserOrdersByUserId(@PathVariable Integer page) {
        Long userId = SecurityUtils.getUserId();
        return orderService.getOrderByUserIdFornotPay(userId, page);
    }

    /**
     * 查询已支付订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrderpayed/{page}")
    public ResponseResult getuserOrderpayedByUserId(@PathVariable Integer page) {
        Long userId = SecurityUtils.getUserId();
        return orderService.getOrderByUserIdForPayed(userId, page);
    }

    /**
     * 查询未发货订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrderUnshopped/{page}")
    public ResponseResult getuserOrderUnshopped(@PathVariable Integer page) {
        Long userId = SecurityUtils.getUserId();
        return orderService.getOrderByUserIdwithStatus(userId, page, SystemConstants.ORDER_UNSHIPPED);
    }

    /**
     * 查询已发货订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrderShopped/{page}")
    public ResponseResult getuserOrderShopped(@PathVariable Integer page) {
        Long userId = SecurityUtils.getUserId();
        return orderService.getOrderByUserIdwithStatus(userId, page, SystemConstants.ORDER_SHIPPED, SystemConstants.ORDER_UNSHIPPED);
    }

    /**
     * 查询退款中订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrderProcessing/{page}")
    public ResponseResult getuserOrderProcessing(@PathVariable Integer page) {
        Long userId = SecurityUtils.getUserId();
        return orderService.getOrderByUserIdwithStatus(userId, page, SystemConstants.REFUND_PROCESSING,
                SystemConstants.REFUND_SUCCESS, SystemConstants.REFUND_ABNORMAL);
    }

    /**
     * 获取用户所有订单
     *
     * @param pages 页码
     * @return
     */
    @RequestMapping("/userALlOrder/{pages}")
    public ResponseResult getuserOrder(@PathVariable Integer pages) {
        Long userId = SecurityUtils.getUserId();
        return orderService.getOrderByUserId(userId, pages);
    }

    /**
     * 用户申请退款
     *
     * @param refundInfo 退款单信息要 包括订单编号 和 退款原因
     * @return
     */
    @PostMapping("/refundOrder")
    public ResponseResult refundOrder(@RequestBody RefundInfo refundInfo) {
        log.info(String.valueOf(refundInfo));
        RefundInfo refundByOrderNoForAliPay = refundInfoService.createRefundByOrderNoForAliPay(refundInfo);
        if (refundByOrderNoForAliPay != null) {
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_USER_IS_NULL);
        }
    }


    /**
     * 确认收货 订单完成
     *
     * @param order 订单 需要订单编号
     * @return
     */
    @PostMapping("/confirmReceipt")
    ResponseResult confirmReceipt(@RequestBody Order order) {
        if (order.getOrderId() != null) {
            order.setOrderStatus(SystemConstants.ORDER_DONE);
            return orderService.updateOrder(order);
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_IS_NULL);
        }
    }

    @GetMapping("/getListforSelL")
    public ResponseResult getListforSelL() {
        List<OrderListforSell> listforSelL = orderService.getListforSelL();
        return ResponseResult.okResult(listforSelL);
    }

}
