package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 角色表(SysRole)表数据库访问层
 *
 * @author ccx
 * @since 2022-07-17 21:16:36
 */
@Mapper
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

}

