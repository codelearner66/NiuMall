package com.hechi.niumall.service;

import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.result.ResponseResult;

public interface LoginService {

    ResponseResult login(SysUser user);
}
