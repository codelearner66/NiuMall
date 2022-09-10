package com.hechi.niumall.service.impl;

import com.hechi.niumall.constants.SystemConstants;
import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.entity.RefundInfo;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.*;
import com.hechi.niumall.utils.OrderNoUtils;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.vo.orderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class BalancePayServiceImpl implements BalancePayService {
    @Autowired
    OrderService orderService;
    @Autowired
    SysUserService sysUserService;

    @Autowired
    PaymentLogService paymentLogService;
    @Autowired
    RefundInfoService refundInfoService;

    private final ReentrantLock lock = new ReentrantLock();

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String tradeCreate(orderVo goods) {

        //生成订单
        ResponseResult order1 = orderService.createOrder(goods);
        //更新订单
        Order data = (Order) order1.getData();
        data.setOrderStatus(SystemConstants.ORDER_PAID);

        String no = OrderNoUtils.getNo();
        data.setPaymentId(no);
        data.setPaymentType(SystemConstants.BALANCE_PAY);

        data.setPaymentTime(new Date());

        if (lock.tryLock()) {
            try {
                ResponseResult result = orderService.updateOrder(data);
                // 扣除用户余额
                LoginUser loginUser = SecurityUtils.getLoginUser();
                SysUser user = loginUser.getUser();
                user.setBalance(user.getBalance() - goods.getSum());
                sysUserService.updataUser(user);
            } finally {
                lock.unlock();
            }

        }

        //更新支付日志
        paymentLogService.createPaymentInfoForBalancePay(data);

        return data.getOrderId();
    }

    @Override
    public void processOrder(Map<String, String> params) {

    }

    @Override
    public void cancelOrder(String orderNo) {

    }

    @Override
    public String queryOrder(String orderNo) {
        return null;
    }

    @Override
    public void checkOrderStatus(String orderNo) {

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refund(RefundInfo refInfo) {
        log.info("调用退款api");
//            创建退款单
        RefundInfo refund = refundInfoService.createRefundByOrderNoForAliPay(refInfo);
        Order orderByOrderNo = orderService.getOrderByOrderNo(refInfo.getOrderNo());
//      更新用户余额
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String totalFee = refund.getTotalFee();
        String substring = totalFee.substring(0, totalFee.length() - 3);
        user.setBalance(user.getBalance() + Long.parseLong(substring));
        boolean b = sysUserService.updataUser(user);
//        更新退款单状态
        if (b) {
            log.info("余额退款成功！");
            refInfo.setRefundStatus(String.valueOf(SystemConstants.REFUND_SUCCESS));
            orderByOrderNo.setOrderStatus(SystemConstants.REFUND_SUCCESS);

        } else {
            log.info("余额退款失败！");
            user.setBalance(user.getBalance() - Long.parseLong(refund.getTotalFee()));
            sysUserService.updataUser(user);
            refInfo.setRefundStatus(String.valueOf(SystemConstants.REFUND_ABNORMAL));
            orderByOrderNo.setOrderStatus(SystemConstants.REFUND_ABNORMAL);
        }
        refundInfoService.updateRefundForAliPay(refInfo);
        orderService.updateOrder(orderByOrderNo);
    }

    @Override
    public String queryRefund(String orderNo) {
        return null;
    }

    @Override
    public String queryBill(String billDate, String type) {
        return null;
    }
}
