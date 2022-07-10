package com.hechi.niumall.filter;

import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.exception.LoginException;
import com.hechi.niumall.utils.JwtUtil;
import com.hechi.niumall.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token = request.getHeader("token");
        //试图获取cookie弥补一直获取请求头中的token信息
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    if((token==null|| "".equals(token))&&cookie.getValue()!=null){
                        token=cookie.getValue();
                    }
                }
            }
        }
        if(!StringUtils.hasText(token)){
            //说明该接口不需要登录  直接放行
            filterChain.doFilter(request, response);
            return;
        }
        LoginUser loginUser = getLoginUser(token, response);
        // 存入SecurityContextHolder 开启权限后在这里将存在redis中的权限信息取出放进下面的第三个参数中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //todo  鉴权 判断用户是否拥有访问该目的地的权限
        //todo redis 维护一个路由权限对照表 每次访问时 查询该路由所需的权限 检查该用户权限集合是否含有该权限做出处理

        filterChain.doFilter(request, response);
    }

    /**
     * 通过解析token获得userid 获取reids 中的用户信息
     * @param token
     * @param response
     * @return
     */
    private LoginUser getLoginUser(String token,HttpServletResponse response) {
        //解析获取userid
        Claims claims;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            //token超时  token非法 响应告诉前端需要重新登录
            throw new LoginException(AppHttpCodeEnum.LOGIN_TOKEN_ERROR);
        }
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("NMLogin:" + userId);
        //如果获取不到
        if(Objects.isNull(loginUser)){
            //登陆超时 提示重新登陆
            throw new LoginException(AppHttpCodeEnum.LOGIN_TIMEOUT);
        }
        if (!loginUser.isAccountNonLocked()){
            // 被锁定或则封禁 提示重新登录
            throw new LoginException(AppHttpCodeEnum.ACCOUNT_LOCKED);
        }
        return loginUser;
    }

}
