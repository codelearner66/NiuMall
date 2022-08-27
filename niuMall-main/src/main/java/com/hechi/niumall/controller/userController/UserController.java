package com.hechi.niumall.controller.userController;

import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    SysUserService sysUserService;

    @RequestMapping("/getUserInfor")
    public ResponseResult getUserInfor(){
     return   sysUserService.getUser();
    }

    @PostMapping("/checkPassword")
  public   ResponseResult checkPassword(@RequestBody SysUser sysUser){
     return   sysUserService.checkPassword(sysUser.getPassword());
    }
}
