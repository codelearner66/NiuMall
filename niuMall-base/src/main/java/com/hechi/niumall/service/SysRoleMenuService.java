package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.SysRoleMenu;

import java.util.List;


/**
 * (SysRoleMenu)表服务接口
 *
 * @author ccx
 * @since 2022-07-17 21:16:55
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {
        public List<SysRoleMenu>  getSysRoleMenuList(List<Long> idList);
}

