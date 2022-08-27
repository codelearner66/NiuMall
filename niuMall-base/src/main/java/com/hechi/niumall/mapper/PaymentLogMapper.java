package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.PaymentLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * nimall商城-支付日志(PaymentLog)表数据库访问层
 *
 * @author ccx
 * @since 2022-08-10 21:34:03
 */
@Mapper
@Repository
public interface PaymentLogMapper extends BaseMapper<PaymentLog> {

}

