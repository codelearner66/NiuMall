package com.hechi.niumall.controller.orders;

import com.hechi.niumall.constants.SystemConstants;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.entity.RefundInfo;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.AlipayService;
import com.hechi.niumall.service.BalancePayService;
import com.hechi.niumall.service.OrderService;
import com.hechi.niumall.service.RefundInfoService;
import com.hechi.niumall.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/manage")
public class OrderQueryController {
    @Autowired
    OrderService orderService;
    @Autowired
    RefundInfoService refundInfoService;
    @Autowired
    AlipayService alipayService;
    @Autowired
    BalancePayService balancePayService;
//    //品类销售图
//    @GetMapping("/getSalesByCategory")
//    public ResponseResult getSalesByCategory(){
//
//
//        orderService.getSalesByCategory();
//
//        return ResponseResult.okResult();
//    }

    /**
     * 管理员查询用户未支付订单
     *
     * @param userId
     * @param page
     * @return
     */
    @RequestMapping("/order/{userId}/{page}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult getOrdersByUserId(@PathVariable Long userId, @PathVariable Integer page) {
        log.info("管理员查询用户未支付订单userId {},页数: {}",userId,page);
        return orderService.getOrderByUserId(userId, page);
    }

    @RequestMapping("/queryOrderByOrderId/{orderId}")
    public ResponseResult queryOrderByOrderId(@PathVariable String orderId){
       return orderService.queryOrderByOrderId(orderId);
    }

    @RequestMapping("/getAllOrder/{pages}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult getAllOrder(@PathVariable Integer pages) {
        return orderService.getAllOrder(pages);
    }

    /**
     * 用户查询未支付订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrder/{page}")
    public ResponseResult getUserOrdersByUserId(@PathVariable Integer page) {
        return orderService.getOrderWithStatus(page, SystemConstants.ORDER_NOT_PAY);
    }

    /**
     * 查询已支付订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrderpayed/{page}")
    public ResponseResult getuserOrderpayedByUserId(@PathVariable Integer page) {
        return orderService.getOrderWithStatus(page, SystemConstants.ORDER_PAID);
    }

    /**
     * 查询未发货订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrderUnshopped/{page}")
    public ResponseResult getuserOrderUnshopped(@PathVariable Integer page) {
        return orderService.getOrderWithStatus(page, SystemConstants.ORDER_UNSHIPPED);
    }

    /**
     * 订单发货
     * @param orderId 订单编号
     * @return
     */
    @RequestMapping("/shopped/{orderId}")
    public ResponseResult shopped(@PathVariable String orderId){
        Order order =new Order();
        order.setOrderId(orderId);
        return orderService.shopped(order);
    }

    /**
     * 查询已发货订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrderShopped/{page}")
    public ResponseResult getuserOrderShopped(@PathVariable Integer page) {
        return orderService.getOrderWithStatus(page, SystemConstants.ORDER_SHIPPED, SystemConstants.ORDER_UNSHIPPED);
    }

    /**
     * 查询退款中订单
     *
     * @param page
     * @return
     */
    @RequestMapping("/userOrderProcessing/{page}")
    public ResponseResult getuserOrderProcessing(@PathVariable Integer page) {
        return orderService.getOrderWithStatus(page, SystemConstants.REFUND_PROCESSING,
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

    @RequestMapping("/getRefundInfo/{orderId}")
    public ResponseResult getRefundInfo(@PathVariable  String orderId){
        RefundInfo refundInfo=new RefundInfo();
        refundInfo.setOrderNo(orderId);
        RefundInfo refundOrderByOrderNo = refundInfoService.getRefundOrderByOrderNo(refundInfo);
        return ResponseResult.okResult(refundOrderByOrderNo);
    }

    /**
     * 审核用户退款申请
     *
     * @param refundInfo 退款单信息要 包括订单编号 和 退款原因
     * @return
     */
    @PostMapping("/refundOrder")
    public ResponseResult refundOrder(@RequestBody RefundInfo refundInfo) {
        log.info(String.valueOf(refundInfo));
        RefundInfo refundByOrderNoForAliPay = refundInfoService.getRefundOrderByOrderNo(refundInfo);
        if (refundByOrderNoForAliPay == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_USER_IS_NULL);
        }
        if (refundByOrderNoForAliPay.getPaymentType().equals(SystemConstants.ALI_PAY)) {
            alipayService.refund(refundByOrderNoForAliPay);
        } else if (refundByOrderNoForAliPay.getPaymentType().equals(SystemConstants.BALANCE_PAY)) {
            balancePayService.refund(refundByOrderNoForAliPay);
        }
        return ResponseResult.okResult();
    }


}
