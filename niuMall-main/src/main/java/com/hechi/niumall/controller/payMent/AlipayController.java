package com.hechi.niumall.controller.payMent;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.entity.RefundInfo;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.AlipayService;
import com.hechi.niumall.service.OrderService;
import com.hechi.niumall.vo.orderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * @author 86176
 * 支付宝支付相关接口
 */
@Slf4j
@RestController
@RequestMapping("/pay/api")
public class AlipayController {
    @Autowired
    AlipayService alipayService;
    @Autowired
    OrderService orderService;
    @Resource
    private Environment config;

    /**
     * 生成订单并将支付宝支付页面返回
     *
     * @param orderVo 商品信息 和 地址信息
     * @return 支付宝支付页面
     */
    @PostMapping("/trade/aliPay")
    public ResponseResult tradePagePayer(@RequestBody orderVo orderVo) {
        String s = alipayService.tradeCreate(orderVo);
        return ResponseResult.okResult(s);
    }

    @GetMapping("/trade/{orderNo}")
    public ResponseResult tradePageByOrderNo(@PathVariable @NotNull String orderNo){
        String s = alipayService.tradeCreateByOrderNo(orderNo);
        return ResponseResult.okResult(s);
    }

    /**
     * 支付宝支付异步通知接口
     * @param params 订单参数
     * @return
     */
    @PostMapping("/trade/notify")
    public ResponseResult tradeNotify(@RequestParam Map<String, String> params) {
        log.info("支付通知正在执行");
        log.info("通知参数 ===> {}", params);
        boolean result = false;
        try {
            boolean b = AlipaySignature.rsaCheckV1(
                    params,
                    config.getProperty("alipay.alipay-public-key"),
                    AlipayConstants.CHARSET_UTF8,
                    AlipayConstants.SIGN_TYPE_RSA2);
            if (!b) {
                //验签失败则记录异常日志，并在response中返回failure.
                log.error("支付成功异步通知验签失败！");
                return ResponseResult.errorResult(AppHttpCodeEnum.ALI_PAY_SIGN_ERROR);
            }
            // 验签成功后
            log.info("支付成功异步通知验签成功！");

            //按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
            //1 商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号
            String outTradeNo = params.get("out_trade_no");
            Order order = orderService.getOrderByOrderNo(outTradeNo);
            if (order == null) {
                log.error("订单不存在");
                return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_IS_NULL);
            }

            //2判断total-amount 是否为该订单的实际金额（订单创建时的金额）
            String totalAmount = params.get("total_amount");
            boolean equals = order.getPayment().equals(totalAmount);
            if (!equals) {
                log.error("金额校验失败");
                return ResponseResult.errorResult(AppHttpCodeEnum.ALI_PAY_TOTAL_AMOUNT_ERROR);
            }
            //3.检验通知中的 seller_id（或者seller_email）是否为 out_trade_no 这笔单据的对应操作方

            String sellerId = params.get("seller_id");
            String sellerIdProperty = config.getProperty("alipay.seller-id");
            if (!sellerId.equals(sellerIdProperty)) {
                log.error("商家pId校验失败");
                return ResponseResult.errorResult(AppHttpCodeEnum.ALI_PAY_SELLERID_ERROR);
            }

            //4.校验app_id是否为商户本身
            String appId = params.get("app_id");
            String appIdProperty = config.getProperty("alipay.app-id");

            assert appIdProperty != null;
            if (!appIdProperty.equals(appId)) {
             log.error("appId校验失败");
             return ResponseResult.errorResult(AppHttpCodeEnum.ALI_PAY_APPID_ERROR);
            }

//            在支付宝业务通知中，只有交易通知状态为TRADE_SUCCESS时支付宝才会认为买家付款成功
            String tradeStatus = params.get("trade_status");
            if (!"TRADE_SUCCESS".equals(tradeStatus)){
                log.error("支付未成功");
                return ResponseResult.errorResult(AppHttpCodeEnum.ALI_PAY_SELL_ERROR);
            }

          // todo 处理业务 修改订单状态 记录支付日志 在订单生成时 放入rabbitMq中利用死信队列做订单超时检测
            alipayService.processOrder(params);
            result=true;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.ALI_PAY_SELL_ERROR);
    }

    /**取消订单
     * @param orderNo 本地生成的订单号 NIUMALL_ORDER_202208111836131940
     * @return
     */
    @PostMapping("/trade/close/{orderNo}")
    public ResponseResult cancelOrder(@PathVariable String orderNo){
        log.info("取消订单");
        alipayService.cancelOrder(orderNo);
        return ResponseResult.okResult();
    }

    /**查询支付宝订单
     * @param orderNo 本地生成的订单号 NIUMALL_ORDER_202208111836131940
     * @return
     */
    @PostMapping("/trade/query/{orderNo}")
    public ResponseResult queryOrder(@PathVariable String orderNo){
        log.info("支付宝订单查询");
        String s = alipayService.queryOrder(orderNo);
        if (Objects.isNull(s)){
            return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_USER_IS_NULL);
        }
        return ResponseResult.okResult(s);
    }

    /**
     * 支付宝退款
     * @param refundInfo 退款单对象 要包括订单编号 和 退款原因
     * @return
     */
    @PostMapping("/trade/refund")
    public ResponseResult refunds(@RequestBody RefundInfo refundInfo){
        log.info("申请退款");
        alipayService.refund(refundInfo);
        return ResponseResult.okResult();
    }

    /**
     * 退款查询
     * @param orderNo 订单编号
     * @return
     */
    @GetMapping("/trade/queryRefund/{orderNo}")
    public ResponseResult queryRefund(@PathVariable @NotNull String orderNo){
        log.info("查询退款");
        String s = alipayService.queryRefund(orderNo);
        if (null == s) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ORDER_USER_IS_NULL);
        }
        else {
            return ResponseResult.okResult(s);
        }
    }
}
