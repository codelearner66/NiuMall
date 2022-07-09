package com.hechi.niumall.controller;

import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.service.SysUserService;
import com.hechi.niumall.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
  @Autowired
    SysUserService sysUserService;
  @Autowired
  RedisCache redisCache;
  @RequestMapping("/test")
    public SysUser getUser() {
    redisCache.setCacheObject("user",sysUserService.getUser());
       return sysUserService.getUser();
    }
}
