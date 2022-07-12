package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.mapper.SysUserMapper;
import com.hechi.niumall.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户表(SysUser)表服务实现类
 *
 * @author ccx
 * @since 2022-07-09 16:29:13
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public SysUser getUser() {
        return getById(1);
    }
    @Override
    public LoginUser getUserByPhoneNumber(String phoneNumber) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getPhonenumber,phoneNumber).or().eq(SysUser::getEmail,phoneNumber);
        SysUser user=getOne(lambdaQueryWrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //查询权限信息封装
        //List<String> userAuthorityList = menuMapper.getUserAuthorityList(user.getId());
        //return new LoginUser(user,userAuthorityList);
        //todo 封装限权
        return new LoginUser(user,null);
    }
}
