package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * niumall商城-订单(Order)表数据库访问层
 *
 * @author ccx
 * @since 2022-08-09 21:59:50
 */
@Mapper
@Repository
public interface OrderMapper extends BaseMapper<Order> {

}
