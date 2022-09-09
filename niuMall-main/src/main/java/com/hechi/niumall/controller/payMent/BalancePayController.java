package com.hechi.niumall.controller.payMent;

import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.RefundInfo;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.BalancePayService;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.vo.orderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * @author ccx
 */
@Slf4j
@RestController
public class BalancePayController {

    @Autowired
    BalancePayService balancePayService;

    //检查帐号余额是否足够
    @RequestMapping("/checkBalance/{value}")
    public ResponseResult checkBalance(@PathVariable long value){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        if (user.getBalance()>=value){
            return ResponseResult.okResult();
        }
        else {
            return ResponseResult.errorResult(AppHttpCodeEnum.BANANCE_NOT_ENOUGH);
        }
    }

    //生成订单
    @PostMapping("/balanceOrder")
    public ResponseResult createOrder(@RequestBody orderVo order){
        String s = balancePayService.tradeCreate(order);
        return ResponseResult.okResult(s);
    }
    //退款
    @PostMapping("/refund/balanceOrder")

    public ResponseResult refundBalanceOrder(@RequestBody RefundInfo refund) {
        log.info("开始退款");
        balancePayService.refund(refund);
        return ResponseResult.okResult();
    }
}
