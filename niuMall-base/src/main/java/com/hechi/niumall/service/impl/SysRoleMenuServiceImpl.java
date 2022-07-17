package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.SysRoleMenu;
import com.hechi.niumall.mapper.SysRoleMenuMapper;
import com.hechi.niumall.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (SysRoleMenu)表服务实现类
 *
 * @author ccx
 * @since 2022-07-17 21:16:55
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
   @Autowired
    SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<SysRoleMenu> getSysRoleMenuList(List<Long> idList) {

        return roleMenuMapper.selectBatchIds(idList);
    }
}
