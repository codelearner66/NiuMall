package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.SysUser;

/**
 * 用户表(SysUser)表服务接口
 *
 * @author ccx
 * @since 2022-07-09 16:29:11
 */
public interface SysUserService extends IService<SysUser> {
    SysUser getUser();
    LoginUser getUserByPhoneNumber(String phoneNumber);

}
