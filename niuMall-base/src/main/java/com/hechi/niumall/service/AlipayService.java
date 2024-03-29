package com.hechi.niumall.service;

import com.hechi.niumall.entity.RefundInfo;
import com.hechi.niumall.vo.orderVo;

import java.util.Map;

public interface AlipayService {
//    生成订单
    String tradeCreate(orderVo goods);

    //    生成订单
    String tradeCreateByOrderNo(String orderNo);

    void processOrder(Map<String, String> params);

    void cancelOrder(String orderNo);

    String queryOrder(String orderNo);

    boolean checkOrderStatus(String orderNo) throws InterruptedException;

    void refund(RefundInfo refInfo);

    String queryRefund(String orderNo);

    String queryBill(String billDate, String type);

}
