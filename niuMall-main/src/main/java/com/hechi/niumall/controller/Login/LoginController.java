package com.hechi.niumall.controller.Login;

import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ccx
 *
 * 负责登陆注册登出
 */
@RestController
public class LoginController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody SysUser sysUser){
        return loginService.login(sysUser);
    }
    @RequestMapping("/getUserInfo")
    public ResponseResult getUserInfo(HttpServletRequest request){
        return  loginService.getUserInfo(request);
    }
    @RequestMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
