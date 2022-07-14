package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.mapper.SysUserMapper;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SysUserService;
import com.hechi.niumall.utils.MailUtils;
import com.hechi.niumall.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Random;

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

    @Autowired
    MessageUtils messageUtils;
    @Autowired
    MailUtils mailUtils;
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

    @Override
    @Transactional
    public ResponseResult findpassword(SysUser user) throws Exception {
        //判断是邮箱还是手机号 选择相应的工具类发送消息
        Random random = new Random();
        int code = random.nextInt(100000);
        SysUser one = null;
        if (user.getPhonenumber()!=null&&user.getEmail()==null){
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser ::getPhonenumber,user.getPhonenumber());
            one = getOne(queryWrapper);
            messageUtils.sentMessage(user.getPhonenumber(),Integer.toString(code));
        }
        if(user.getEmail()!=null&&user.getPhonenumber()==null) {
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser ::getEmail,user.getEmail());
            one = getOne(queryWrapper);
            mailUtils.sentMailCode(user.getEmail(), "重置密码","mallNewPassword",Integer.toString(code));
        }
        assert one != null;
        one.setPassword(passwordEncoder.encode(Integer.toString(code)));
        return updateById(one)? ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}
