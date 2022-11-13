package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * niumall商城-订单(Order)表实体类
 *
 * @author ccx
 * @since 2022-08-09 23:11:38
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("niumall.order")
public class Order implements Serializable {
    @TableId
    private String id;

    //用户id
    private Long userId;
    //地址id
    private Long addrId;
    //订单编号
    private String orderId;
    //商品id
    private Long goodsId;
    //订单详情，商品信息
    private String orderContent;
    //实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
    private String payment;
    //支付流水id
    private String paymentId;
    //支付类型，1、货到付款，2、在线支付，3、微信支付，4、支付宝支付
    private Integer paymentType;
    //邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分
    private String postFee;
    //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
    private Integer orderStatus;
    //物流名称
    private String shippingName;
    //物流单号
    private String shippingCode;
    //退换无忧
    private String noAnnoyance;
    //服务费
    private String servicePrice;
    //返现
    private String returnPrice;
    //订单总重 单位/克
    private String totalWeight;
    //买家是否已经评价
    private Integer buyerRate;
    //交易关闭时间
    private Date closeTime;
    //交易完成时间
    private Date endTime;
    //付款时间
    private Date paymentTime;
    //发货时间
    private Date consignTime;
    //订单创建时间
    private Date createTime;

}
