package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * (RefundInfo)表实体类
 *
 * @author ccx
 * @since 2022-08-23 22:11:56
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("refund_info")
public class RefundInfo  {
    //退款单id@TableId
    private String id;
    //用户id
    private Long userId;
    //商户订单编号
    private String orderNo;
    //商户退款单编号
    private String refundNo;
    //支付系统退款单号
    private String refundId;
    //支付类型
    private Integer paymentType;
    //原订单金额(分)
    private String totalFee;
    //退款金额(分)
    private String refund;
    //退款原因
    private String reason;
    //退款状态
    private String refundStatus;
    //申请退款返回参数
    private String contentReturn;
    //退款结果通知参数
    private String contentNotify;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;




}
