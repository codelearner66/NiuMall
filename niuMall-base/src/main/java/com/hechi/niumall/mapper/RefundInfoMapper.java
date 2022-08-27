package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.RefundInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * (RefundInfo)表数据库访问层
 *
 * @author ccx
 * @since 2022-08-22 15:10:49
 */
@Mapper
@Repository
public interface RefundInfoMapper extends BaseMapper<RefundInfo> {

}

