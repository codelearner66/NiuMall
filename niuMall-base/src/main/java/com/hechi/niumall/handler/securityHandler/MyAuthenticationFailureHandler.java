package com.hechi.niumall.handler.securityHandler;

import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        WebUtils.responseUtils(response, AppHttpCodeEnum.LOGIN_ERROR,"登陆失败");
    }
}
