package com.hechi.niumall.handler.securityHandler;

import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.utils.JwtUtil;
import com.hechi.niumall.utils.RedisCache;
import com.hechi.niumall.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理验证码验证登录成功处理
 * @Author: ccx
 * @Date: 2021-09-09 9:21
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    RedisCache redisCache;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        LoginUser user = (LoginUser) authentication.getPrincipal();
        System.out.println("获取的用户信息：  ===》"+user);
        String id = user.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(id);
        //todo 将登陆成功的用户信息存到redis中 并将 验证码销毁
        LoginUser loginUser = redisCache.getCacheObject("NMLogin:" + id);
        //检查该用户是否已经存在
        if(loginUser != null){
            redisCache.deleteObject("NMLogin:" + id);
        }
        SysUser baseUser = user.getUser();
        if (baseUser.getPhonenumber()!=null) {
            redisCache.deleteObject("message"+baseUser.getPhonenumber());
        }
        if (baseUser.getEmail()!=null) {
            redisCache.deleteObject("message"+baseUser.getEmail());
        }
        //把用户信息存入redis
        redisCache.setCacheObject("NMLogin:" + id, user);
        WebUtils.responseUtils(response, AppHttpCodeEnum.SUCCESS,jwt);
    }
}
