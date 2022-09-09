package com.hechi.niumall.controller.orders;

import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.OrderService;
import com.hechi.niumall.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * 管理员查询用户未支付订单
     * @param userId
     * @param page
     * @return
     */
    @RequestMapping("/order/{userId}/{page}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult getOrdersByUserId(@PathVariable Long userId,@PathVariable Integer page){
        return orderService.getOrderByUserIdFornotPay(userId, page);
    }

    /**
     * 用户查询未支付订单
     * @param page
     * @return
     */
    @RequestMapping("/userOrder/{page}")
    public ResponseResult getUserOrdersByUserId(@PathVariable Integer page){
        Long userId = SecurityUtils.getUserId();
        return orderService.getOrderByUserIdFornotPay(userId, page);
    }


}
