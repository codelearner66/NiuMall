package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * niumall商城-商品分类(GoodsCategory)表数据库访问层
 *
 * @author ccx
 * @since 2022-07-14 21:48:55
 */
@Mapper
@Repository
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {

}

