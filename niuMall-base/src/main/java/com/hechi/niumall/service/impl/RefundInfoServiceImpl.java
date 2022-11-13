package com.hechi.niumall.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.constants.SystemConstants;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.entity.RefundInfo;
import com.hechi.niumall.mapper.RefundInfoMapper;
import com.hechi.niumall.service.OrderService;
import com.hechi.niumall.service.RefundInfoService;
import com.hechi.niumall.utils.OrderNoUtils;
import com.hechi.niumall.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * (RefundInfo)表服务实现类
 *
 * @author ccx
 * @since 2022-08-22 15:10:49
 */
@Service("refundInfoService")
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {
    @Autowired
    OrderService orderService;

    /**
     * 新建退款单(微信)
     *
     * @param refInfo 订单信息 包括 用户订单id 退款原因  paymentType支付类型（余额支付或支付宝支付）
     * @return 订单信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RefundInfo createRefundByOrderNo(RefundInfo refInfo) {
        LambdaQueryWrapper<RefundInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundInfo::getOrderNo, refInfo.getOrderNo())
                .eq(RefundInfo::getRefundStatus, String.valueOf(SystemConstants.REFUND_PROCESSING));
        RefundInfo one = getOne(wrapper);
        if (one != null) {
            return one;
        }
        Order orderByOrderNo = orderService.getOrderByOrderNo(refInfo.getOrderNo());
//        设置用户id
        refInfo.setUserId(SecurityUtils.getUserId());
//        退款单编号
        refInfo.setRefundNo(OrderNoUtils.getRefundNo());
//        原订单金额
        refInfo.setTotalFee(orderByOrderNo.getPayment());
//        设置退款类型 和支付类型相同
        refInfo.setPaymentType(orderByOrderNo.getPaymentType());
//        设置退款单状态
        refInfo.setRefundStatus(String.valueOf(SystemConstants.REFUND_PROCESSING));
//        设置订单状态
        orderByOrderNo.setOrderStatus(SystemConstants.REFUND_PROCESSING);
//      退款金额  退款金额可以指定
        refInfo.setRefund(orderByOrderNo.getPayment());
        refInfo.setCreateTime(new Date());
        orderService.updateOrder(orderByOrderNo);
        boolean save = save(refInfo);
        if (!save) {
            return null;
        }
        return refInfo;
    }

    /**
     * 更新退款状态（微信）
     *
     * @param content
     */
    @Override
    public void updateRefund(String content) {
//        将json字符串 转化为 Map
        HashMap<String, String> hashMap = JSON.parseObject(content, HashMap.class);
        //根据退款单编号修改退款单
        LambdaQueryWrapper<RefundInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefundInfo::getRefundNo, hashMap.get("out_refund_no"));
        //设置要修改的字段
        RefundInfo refundInfo = new RefundInfo();

        refundInfo.setRefundId(hashMap.get("refund_id"));//微信支付退款单号

        //查询退款和申请退款中的返回参数
        if (hashMap.get("status") != null) {
            refundInfo.setRefundStatus(hashMap.get("status"));//退款状态
            refundInfo.setContentReturn(content);//将全部响应结果存入数据库的content字段
        }
        //退款回调中的回调参数
        if (hashMap.get("refund_status") != null) {
            refundInfo.setRefundStatus(hashMap.get("refund_status"));//退款状态
            refundInfo.setContentNotify(content);//将全部响应结果存入数据库的content字段
        }

        //更新退款单
        baseMapper.update(refundInfo, wrapper);
    }

    @Override
    public List<RefundInfo> getNoRefundOrderByDuration(int minutes) {
        return null;
    }

    @Override
    public RefundInfo createRefundByOrderNoForAliPay(RefundInfo refInfo) {
        //   退款单已存在时直接返回该订单
        return this.createRefundByOrderNo(refInfo);
    }

    @Override
    public void updateRefundForAliPay(RefundInfo refInfo) {
        LambdaQueryWrapper<RefundInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RefundInfo::getRefundNo, refInfo.getRefundNo());
        baseMapper.update(refInfo, queryWrapper);
    }
}
