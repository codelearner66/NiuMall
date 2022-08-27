package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.ShopCart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * niuMall商城-购物车(ShopCart)表数据库访问层
 *
 * @author ccx
 * @since 2022-08-23 22:48:12
 */
@Mapper
@Repository
public interface ShopCartMapper extends BaseMapper<ShopCart> {

}

