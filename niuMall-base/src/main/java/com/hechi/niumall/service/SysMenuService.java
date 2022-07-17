package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.SysMenu;

import java.util.List;


/**
 * 菜单表(SysMenu)表服务接口
 *
 * @author ccx
 * @since 2022-07-17 21:18:05
 */
public interface SysMenuService extends IService<SysMenu> {
  public List<SysMenu> getSysMenuList(List<Long> idList);
}

