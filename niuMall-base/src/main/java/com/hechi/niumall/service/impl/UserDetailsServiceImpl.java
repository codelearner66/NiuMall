package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hechi.niumall.entity.*;
import com.hechi.niumall.mapper.SysUserMapper;
import com.hechi.niumall.service.SysMenuService;
import com.hechi.niumall.service.SysRoleMenuService;
import com.hechi.niumall.service.SysRoleService;
import com.hechi.niumall.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ccx
 *
 * 在springboot security 中通过用户姓名获取用户信息
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysRoleMenuService sysRoleMenuService;
    @Autowired
    SysMenuService sysMenuService;
    @Autowired
    SysUserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUserName,username);
        SysUser user = userMapper.selectOne(queryWrapper);
        //判断是否查到用户  如果没查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        List<SysUserRole> sysUserRolesByUserId = sysUserRoleService.getSysUserRolesByUserId(user.getId());
        List<Long> collect = sysUserRolesByUserId.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        collect.forEach(System.out::println);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.getSysRoleMenuList(collect);
        List<Long> collect1 = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        List<SysMenu> sysMenuList = sysMenuService.getSysMenuList(collect1);
        List<String> collect2 = sysMenuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        //返回用户信息
        //查询权限信息封装
        return new LoginUser(user,collect2);
    }
}
