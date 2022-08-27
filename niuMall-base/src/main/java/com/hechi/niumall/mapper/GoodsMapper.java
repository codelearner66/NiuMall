package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * niumall商城-商品(Goods)表数据库访问层
 *
 * @author ccx
 * @since 2022-07-25 10:06:01
 */
@Mapper
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {

}

