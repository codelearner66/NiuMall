package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 菜单表(SysMenu)表数据库访问层
 *
 * @author ccx
 * @since 2022-07-17 21:18:05
 */
@Mapper
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

}

