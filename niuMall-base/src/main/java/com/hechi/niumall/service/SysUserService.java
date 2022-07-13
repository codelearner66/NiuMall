package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.result.ResponseResult;

/**
 * 用户表(SysUser)表服务接口
 *
 * @author ccx
 * @since 2022-07-09 16:29:11
 */
public interface SysUserService extends IService<SysUser> {
    SysUser getUser();
    /**
     * 通过 手机号或邮箱查询用户信息
     * @param phoneNumber
     * @return
     */
    LoginUser getUserByPhoneNumber(String phoneNumber);
    /**
     * 判断账号是否存在
     * @param user
     * @return
     */
    ResponseResult isExist(SysUser user);

    /**
     * 添加新用户
     * @param user
     * @return
     */
    ResponseResult addUser(SysUser user);
}
