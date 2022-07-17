package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.SysRole;
import com.hechi.niumall.mapper.SysRoleMapper;
import com.hechi.niumall.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色表(SysRole)表服务实现类
 *
 * @author ccx
 * @since 2022-07-17 21:16:36
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Override
    public List<SysRole> findByRoleId(List<Long> idList) {

        return sysRoleMapper.selectBatchIds(idList);
    }
}
