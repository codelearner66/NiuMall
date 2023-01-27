package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.RefundInfo;


/**
 * (RefundInfo)表服务接口
 *
 * @author ccx
 * @since 2022-08-22 15:10:49
 */
public interface RefundInfoService extends IService<RefundInfo> {

    RefundInfo createRefundByOrderNo(RefundInfo refInfo);

    void updateRefund(String content);

    RefundInfo getRefundOrderByOrderNo(RefundInfo refInfo);

    RefundInfo createRefundByOrderNoForAliPay(RefundInfo refInfo);

    void updateRefundForAliPay(RefundInfo refInfo);
}

