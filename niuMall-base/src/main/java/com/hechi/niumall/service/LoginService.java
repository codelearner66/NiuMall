package com.hechi.niumall.service;

import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.result.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    ResponseResult login(SysUser user);

    ResponseResult getUserInfo(HttpServletRequest request);

    ResponseResult logout();
}
