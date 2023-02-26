package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.GoodsComment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 商品评价(GoodsComment)表数据库访问层
 *
 * @author ccx
 * @since 2023-02-21 15:25:22
 */
@Mapper
@Repository
public interface GoodsCommentMapper extends BaseMapper<GoodsComment> {

}

