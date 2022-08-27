package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.entity.PaymentLog;

import java.util.Map;


/**
 * nimall商城-支付日志(PaymentLog)表服务接口
 *
 * @author ccx
 * @since 2022-08-10 21:34:03
 */
public interface PaymentLogService extends IService<PaymentLog> {

    void createPaymentInfo(String plainText);

    void createPaymentInfoForAliPay(Map<String, String> params);

    void createPaymentInfoForBalancePay(Order order);
}

