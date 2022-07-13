package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.mapper.SysUserMapper;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    PasswordEncoder passwordEncoder;

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


    @Override
    public ResponseResult isExist(SysUser user) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser ::getUserName,user.getUserName()!=null?user.getUserName():" ")
                    .or().eq(SysUser ::getEmail,user.getEmail()!=null?user.getEmail():"-1")
                    .or().eq(SysUser ::getPhonenumber,user.getPhonenumber()!=null?user.getPhonenumber():"-1");
            SysUser one = getOne(wrapper);
           return one!=null? ResponseResult.okResult(200,"帐号存在"): ResponseResult.errorResult(200,"帐号不存在");
    }

    @Override
    public ResponseResult addUser(SysUser user) {
        if("帐号存在".equals(isExist(user).getMsg())){
         return    ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return  save(user)?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
    }
}
