package com.hechi.niumall.filter.messageLoginFilter;

import com.hechi.niumall.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

   private SysUserService userService;

    public EmailCodeAuthenticationProvider(SysUserService userService) {
        this.userService = userService;
    }


    /**
     * 认证
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        EmailCodeAuthenticationToken token = (EmailCodeAuthenticationToken) authentication;
        String principal = (String) token.getPrincipal();
        UserDetails user = userService.getUserByPhoneNumber(principal);
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        System.out.println(user.getAuthorities());
        EmailCodeAuthenticationToken result = new EmailCodeAuthenticationToken(user, user.getAuthorities());
                /*
                Details 中包含了 ip地址、 sessionId 等等属性 也可以存储一些自己想要放进去的内容
                */
        result.setDetails(token.getDetails());
        return result;
    }
    @Override
    public boolean supports(Class<?> aClass) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}