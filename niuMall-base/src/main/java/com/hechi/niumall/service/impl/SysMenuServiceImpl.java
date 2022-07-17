package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.SysMenu;
import com.hechi.niumall.mapper.SysMenuMapper;
import com.hechi.niumall.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单表(SysMenu)表服务实现类
 *
 * @author ccx
 * @since 2022-07-17 21:18:05
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
   @Autowired
    SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> getSysMenuList(List<Long> idList) {
        return sysMenuMapper.selectBatchIds(idList);
    }
}
