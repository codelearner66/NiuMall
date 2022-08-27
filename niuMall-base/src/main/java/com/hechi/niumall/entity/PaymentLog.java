package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * nimall商城-支付日志(PaymentLog)表实体类
 *
 * @author ccx
 * @since 2022-08-10 21:34:03
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("payment_log")
public class PaymentLog  {
    @TableId
    private String id;

    //用户id
    private Integer userId;
    //订单id
    private String orderId;
    //支付平台交易号
    private String tradeNo;
    //支付平台用户帐号
    private String buyerUser;
    //支付金额
    private Double payPrice;
    //订单实际金额
    private Double totalPrice;
    //订单名称
    private String payLogSubject;
    //支付方式标记
    private String payment;
    //支付方式名称
    private String paymentName;
    //业务类型（0默认, 1订单, 2充值, ...）
    private Integer businessType;
    //付款时间
    private Date paymentTime;



}
