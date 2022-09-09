package com.hechi.niumall.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.google.gson.internal.LinkedTreeMap;
import com.hechi.niumall.constants.SystemConstants;
import com.hechi.niumall.entity.Goods;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.entity.RefundInfo;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.enums.AliPayTradeState;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.*;
import com.hechi.niumall.vo.orderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    PaymentLogService paymentLogService;

    @Autowired
    RefundInfoService refundInfoService;
    @Autowired
    SysUserService sysUserService;
    @Resource
    private Environment config;

    private final ReentrantLock lock = new ReentrantLock();

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String tradeCreate(orderVo goods) {
        try {
            log.info("生成订单");
            ResponseResult result = orderService.createOrder(goods);
            Order order = (Order) result.getData();
            //调用支付宝接口
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

            //配置需要的公共请求参数
            //支付完成后，支付宝发起异步通知的地址
            request.setNotifyUrl(config.getProperty("alipay.notify-url"));
            //支付完成后，我们想让页面跳转的页面，配置returnUrl
            request.setReturnUrl(config.getProperty("alipay.return-url"));

            //组装当前业务方法的请求参数
            JSONObject bizContent = new JSONObject();

            bizContent.put("out_trade_no", order.getOrderId());
            bizContent.put("total_amount", order.getPayment());
            Goods goods1 = JSON.parseObject(order.getOrderContent(), Goods.class);
            bizContent.put("subject", goods1.getTitle());
            bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

            request.setBizContent(bizContent.toString());
            //    执行请求 调用支付宝接口
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
                return response.getBody();
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                throw new RuntimeException("创建支付交易失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("创建支付交易失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processOrder(Map<String, String> params) {
        log.info("处理订单");
        //获取订单号
        String outTradeNo = params.get("out_trade_no");

        /*在对业务数据进行状态检查和处理之前，
        要采用数据锁进行并发控制，
        以避免函数重入造成的数据混乱*/
        //尝试获取锁：
        // 成功获取则立即返回true，获取失败则立即返回false。不必一直等待锁的释放

        if (lock.tryLock()) {
            try {
                Order orderByOrderNo = orderService.getOrderByOrderNo(outTradeNo);
//                if (SystemConstants.ORDER_NOT_PAY == orderByOrderNo.getOrderStatus()) {
//                    //未支付
//                    return;
//                }
                //  更新订单状态
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    orderByOrderNo.setOrderStatus(SystemConstants.ORDER_PAID);
                }
                String tradeNo = params.get("trade_no");
                orderByOrderNo.setPaymentId(tradeNo);

                orderByOrderNo.setPaymentTime(new Date());

                orderService.updateOrder(orderByOrderNo);
                 //  果是余额充值就更新用户余额
                if (orderByOrderNo.getGoodsId() == 0L) {
                    SysUser byId = sysUserService.getById(orderByOrderNo.getUserId());
                    if (byId != null) {
                        String payment = orderByOrderNo.getPayment();
                        String replace = payment.replace(".00", "");
                        byId.setBalance(byId.getBalance() + Long.parseLong(replace));
                        sysUserService.updataUser(byId);
                    }

                }
                //  记录支付日志
                paymentLogService.createPaymentInfoForAliPay(params);
            } finally {
//                主动释放锁
                lock.unlock();
            }

        }

    }

    /**
     * 关单接口
     *
     * @param orderNo
     */
    @Override
    public void cancelOrder(String orderNo) {
//       关单
        try {
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            request.setBizContent(String.valueOf(bizContent));
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                //throw new RuntimeException("关单接口的调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("关单接口的调用失败");
        }
//        本地数据库订单状态更新
        Order order = new Order();
        order.setOrderId(orderNo);
        order.setOrderStatus(SystemConstants.ORDER_USER_CLOSED);
        orderService.updateOrder(order);
    }

    /**
     * 查单接口
     *
     * @param orderNo
     * @return
     */
    @Override
    public String queryOrder(String orderNo) {
        try {
            log.info("查单接口调用====》{}", orderNo);
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            request.setBizContent(bizContent.toString());
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
                return response.getBody();
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                //throw new RuntimeException("查单接口的调用失败");
                return null;//订单不存在
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("查单接口的调用失败");
        }
    }

    /**
     * 根据订单号调用支付宝查单接口，核实订单状态
     * 如果订单未创建，则更新商户端订单状态
     * 如果订单未支付，则调用关单接口关闭订单，并更新商户端订单状态
     * 如果订单已支付，则更新商户端订单状态，并记录支付日志
     * 后期结合rabbitmq 通过死信队列做自动查单 超时关单操作
     *
     * @param orderNo
     */
    @Override
    public void checkOrderStatus(String orderNo) {
        log.warn("根据订单号核实订单状态 ===> {}", orderNo);

        String result = this.queryOrder(orderNo);
        if (result == null) {
            log.warn("核实订单未创建======》{}", orderNo);
            Order order = new Order();
            order.setOrderId(orderNo);
            order.setOrderStatus(SystemConstants.ORDER_CLOSED);
            //  更新本地订单状态
            orderService.updateOrder(order);
            return;
        }

//        解析查单响应结果
        System.out.println(result);
        HashMap<String, LinkedTreeMap> hashMap = JSON.parseObject(result, HashMap.class);

        LinkedTreeMap alipayTradeQueryResponse = hashMap.get("alipay_trade_query_response");

        String tradeStatus = (String) alipayTradeQueryResponse.get("trade_status");
        if (AliPayTradeState.NOTPAY.getType().equals(tradeStatus)) {
            log.warn("核实订单未支付 ===> {}", orderNo);
            this.cancelOrder(orderNo);
        }
        if (AliPayTradeState.SUCCESS.getType().equals(tradeStatus)) {
            log.warn("核实订单已支付 ===> {}", orderNo);
            //如果订单已支付，则更新商户端订单状态
            Order order = new Order();
            order.setOrderId(orderNo);
            order.setOrderStatus(SystemConstants.ORDER_PAID);
            orderService.updateOrder(order);
            //并记录支付日志

            paymentLogService.createPaymentInfoForAliPay(alipayTradeQueryResponse);
        }

    }

    /**
     * 退款接口
     *
     * @param refInfo orderNo 订单编号 reason 退款原因
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refund(RefundInfo refInfo) {
        try {
            log.info("调用退款api");
//            创建退款单
            RefundInfo refundByOrderNoForAliPay = refundInfoService.createRefundByOrderNoForAliPay(refInfo);

//            调用统一收单退款接口
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
//            组装请求数据
            JSONObject bizContent = new JSONObject();
//            订单编号
            bizContent.put("out_trade_no", refundByOrderNoForAliPay.getOrderNo());
//            退款金额
            bizContent.put("refund_amount", refundByOrderNoForAliPay.getRefund());
//            退款原因
            bizContent.put("refund_reason", refundByOrderNoForAliPay.getReason());
            request.setBizContent(bizContent.toString());

//            执行请求，调用支付宝接口
            AlipayTradeRefundResponse response = alipayClient.execute(request);

            Order orderByOrderNo = orderService.getOrderByOrderNo(refInfo.getOrderNo());
            refundByOrderNoForAliPay.setContentReturn(response.getBody());
            if (response.isSuccess()) {
                log.info("调用成功，返回结果=====>{}", response.getBody());
//                更新订单状态
                orderByOrderNo.setOrderStatus(SystemConstants.REFUND_SUCCESS);
//                更新退款单
                refundByOrderNoForAliPay.setRefundStatus(String.valueOf(SystemConstants.REFUND_SUCCESS));

            } else {
                log.info("调用失败，返回码=====>" + response.getCode() + ",返回描述======>" + response.getMsg());
                //                更新订单状态 退款状态异常
                orderByOrderNo.setOrderStatus(SystemConstants.REFUND_ABNORMAL);
                refundByOrderNoForAliPay.setRefundStatus(String.valueOf(SystemConstants.REFUND_ABNORMAL));
            }

            orderService.updateOrder(orderByOrderNo);
            refundInfoService.updateRefundForAliPay(refundByOrderNoForAliPay);

        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("创建退款申请失败");
        }

    }

    /**
     * 查询退款
     *
     * @param orderNo
     * @return
     */
    @Override
    public String queryRefund(String orderNo) {
        try {
            log.info("查询退款接口调用 ===> {}", orderNo);

            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            bizContent.put("out_request_no", orderNo);
            request.setBizContent(bizContent.toString());

            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
                return response.getBody();
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                //throw new RuntimeException("查单接口的调用失败");
                return null;//订单不存在
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("查单接口的调用失败");
        }
    }

    /**
     * 申请账单
     *
     * @param billDate
     * @param type
     * @return
     */
    @Override
    public String queryBill(String billDate, String type) {
        return null;
    }
}
