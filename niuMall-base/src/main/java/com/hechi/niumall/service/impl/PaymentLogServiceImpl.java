package com.hechi.niumall.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.Goods;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.entity.PaymentLog;
import com.hechi.niumall.mapper.PaymentLogMapper;
import com.hechi.niumall.service.PaymentLogService;
import com.hechi.niumall.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * nimall商城-支付日志(PaymentLog)表服务实现类
 *
 * @author ccx
 * @since 2022-08-10 21:34:03
 */
@Slf4j
@Service("paymentLogService")
public class PaymentLogServiceImpl extends ServiceImpl<PaymentLogMapper, PaymentLog> implements PaymentLogService {
    @Override
    public void createPaymentInfo(String plainText) {
        HashMap hashMap = JSON.parseObject(plainText, HashMap.class);

        createPaymentInfoForAliPay(hashMap);
    }

    @Override
    public void createPaymentInfoForAliPay(Map<String, String> params) {
        log.info("记录支付日志");

        /*
         * {
         *   "msg": "Success",
         *   "code": "10000",
         *   "buyer_user_id": "2088622958128812",
         *   "send_pay_date": "2022-10-29 23:29:29",
         *   "invoice_amount": "0.00",
         *   "out_trade_no": "NIUMALL_ORDER_2022102923290651374",
         *   "total_amount": "8000.00",
         *   "buyer_user_type": "PRIVATE",
         *   "trade_status": "TRADE_SUCCESS",
         *   "trade_no": "2022102922001428810501899533",
         *   "buyer_logon_id": "fjr***@sandbox.com",
         *   "receipt_amount": "0.00",
         *   "point_amount": "0.00",
         *   "buyer_pay_amount": "0.00"
         * }
         */
        //获取订单号
        String orderNo = params.get("out_trade_no");
        //业务编号
        String transactionId = params.get("trade_no");
        //交易状态
        String tradeStatus = params.get("trade_status");
        //交易金额
        String totalAmount = params.get("total_amount");
//        订单主题
        String subject = params.get("subject");
        // 支付平台用户帐号
        String buyerId = params.get("buyer_id");
//        付款时间
        String gmtPayment = params.get("gmt_payment");

        PaymentLog paymentLog=new PaymentLog();
//        封装数据
//        匿名用户 无法获取用户id
        paymentLog.setUserId(1);

        paymentLog.setOrderId(orderNo);

        paymentLog.setTradeNo(transactionId);

        paymentLog.setBuyerUser(buyerId);

        paymentLog.setPayPrice(Double.valueOf(totalAmount));

        paymentLog.setTotalPrice(Double.valueOf(totalAmount));

        paymentLog.setPayLogSubject(subject);
        //todo 动态修改
        paymentLog.setPayment("支付宝支付");

        paymentLog.setPaymentName("支付宝支付");

        paymentLog.setBusinessType(1);

        paymentLog.setPaymentTime(new Date());
//       插入数据库
        baseMapper.insert(paymentLog);
    }

    @Override
    public void createPaymentInfoForBalancePay(Order order) {

        PaymentLog paymentLog=new PaymentLog();
        Long userId = SecurityUtils.getUserId();
        paymentLog.setUserId(Math.toIntExact(userId));

        paymentLog.setOrderId(order.getOrderId());

        paymentLog.setTradeNo(order.getPaymentId());

        paymentLog.setBuyerUser(String.valueOf(userId));

        paymentLog.setPayPrice(Double.valueOf(order.getPayment()));

        paymentLog.setTotalPrice(Double.valueOf(order.getPayment()));
        String orderContent = order.getOrderContent();
        Goods goods = JSON.parseObject(orderContent, Goods.class);
        paymentLog.setPayLogSubject(goods.getTitle());
        paymentLog.setPayment("余额支付");

        paymentLog.setPaymentName("余额支付");

        paymentLog.setBusinessType(1);

        paymentLog.setPaymentTime(new Date());
//       插入数据库
        baseMapper.insert(paymentLog);
    }
}
