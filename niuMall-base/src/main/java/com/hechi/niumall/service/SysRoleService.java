package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.SysRole;

import java.util.List;


/**
 * 角色表(SysRole)表服务接口
 *
 * @author ccx
 * @since 2022-07-17 21:16:36
 */
public interface SysRoleService extends IService<SysRole> {
    public List<SysRole>  findByRoleId(List<Long> idList);
}

