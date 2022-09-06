package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.SysUserRole;

import java.util.List;


/**
 * (SysUserRole)表服务接口
 *
 * @author ccx
 * @since 2022-07-17 21:16:06
 */
public interface SysUserRoleService extends IService<SysUserRole> {
    public List<SysUserRole> getSysUserRolesByUserId(Long id);
    boolean upDataUserRole(SysUserRole userRole);
}

