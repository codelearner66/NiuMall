package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户表(SysUser)表数据库访问层
 *
 * @author ccx
 * @since 2022-07-09 16:29:07
 */
@Mapper
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

}
