package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.UserAddr;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * niumall商城-用户地址(UserAddr)表数据库访问层
 *
 * @author ccx
 * @since 2022-08-06 17:32:21
 */
@Mapper
@Repository
public interface UserAddrMapper extends BaseMapper<UserAddr> {

}

