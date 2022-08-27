package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.Areadata;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * (Areadata)表数据库访问层
 *
 * @author ccx
 * @since 2022-08-03 23:54:10
 */
@Mapper
@Repository
public interface AreadataMapper extends BaseMapper<Areadata> {

}

