package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.SysUserRole;
import com.hechi.niumall.mapper.SysUserRoleMapper;
import com.hechi.niumall.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (SysUserRole)表服务实现类
 *
 * @author ccx
 * @since 2022-07-17 21:16:06
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
    @Override
    public List<SysUserRole> getSysUserRolesByUserId(Long id) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId, id);
        return list(queryWrapper);
    }
}
