package com.hechi.niumall.constants;

/**
 * @author ccx
 * 常用静态变量
 */
public class SystemConstants {


    /**
     * 支付类型 0 余额支付
     */
    public static final int BALANCE_PAY = 0;
    /*
     *  1 支付宝支付
     */
    public static final int ALI_PAY = 1;

    /**
     * order_status 支付状态
     * 未支付
     */
    public static final int ORDER_NOT_PAY = 1;
    /**
     * 已支付
     */
    public static final int ORDER_PAID = 2;
    /**
     * 未发货
     */
    public static final int ORDER_UNSHIPPED = 3;
    /**
     * 已发货
     */
    public static final int ORDER_SHIPPED=4;
    /**
     * 交易成功
     */
    public static final int ORDER_DONE=5;
    /**
     * 交易关闭
     */
    public static final int ORDER_CLOSED=6;
//用户取消订单
    public static final int ORDER_USER_CLOSED=7;
    /**
     * 退款中
     */
    public  static final int REFUND_PROCESSING=8;


    /**
     * 已退款
     */
    public  static final int REFUND_SUCCESS=9;

    /**
     * 退款异常
     */
    public  static final int  REFUND_ABNORMAL=10;

}