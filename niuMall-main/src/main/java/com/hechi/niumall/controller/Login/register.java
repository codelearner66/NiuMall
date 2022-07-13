package com.hechi.niumall.controller.Login;

import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class register {
@Autowired
    SysUserService sysUserService;
    //username password 注册
    @PostMapping("/baseRegister")
    public ResponseResult baseResult(@RequestBody @Valid SysUser sysUser){
       sysUserService.addUser(sysUser);
       return ResponseResult.okResult();
    }
    //手机号
    @PostMapping("/phoneRegister")
    public ResponseResult phoneRegister(@RequestBody SysUser sysUser){


        return ResponseResult.okResult();
    }
    // 邮箱注册
    @PostMapping("/mailRegister")
    public ResponseResult mailRegister(@RequestBody SysUser sysUser){


        return ResponseResult.okResult();
    }

}
