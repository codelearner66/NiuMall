package com.hechi.niumall.service.impl;

import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.LoginService;
import com.hechi.niumall.utils.BeanCopyUtils;
import com.hechi.niumall.utils.JwtUtil;
import com.hechi.niumall.utils.RedisCache;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.vo.UserInfoVo;
import com.hechi.niumall.vo.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    static
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult login(SysUser user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate;
        //不捕获可能的认证异常 抛出后统一处理
        authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //从redis中获取用户信息  用于检查用户是否处于异常状态
        LoginUser oldLoginUser = redisCache.getCacheObject("NMLogin:" + userId);
        if (Objects.nonNull(oldLoginUser)&&!oldLoginUser.isAccountNonLocked()){
            // 被锁定或则封禁 提示重新登录
            return ResponseResult.errorResult(AppHttpCodeEnum.ACCOUNT_LOCKED);
        }
        //把用户信息存入redis
        redisCache.setCacheObject("NMLogin:" + userId, loginUser);
        //把token和userinfo封装 返回
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        UserLoginVo vo = new  UserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(vo);
    }
    @Override
    public ResponseResult logout() {
        // 解析获取userid
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //获取userid
        Long userId = loginUser.getUser().getId();
        //从redis中获取用户信息
        LoginUser oldLoginUser = redisCache.getCacheObject("NMLogin:" + userId);
        if (!oldLoginUser.isAccountNonLocked()){
            // 被锁定或则封禁 提示重新登录
            return ResponseResult.errorResult(AppHttpCodeEnum.ACCOUNT_LOCKED);
        }
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:" + userId);
        return ResponseResult.okResult();
    }

}
