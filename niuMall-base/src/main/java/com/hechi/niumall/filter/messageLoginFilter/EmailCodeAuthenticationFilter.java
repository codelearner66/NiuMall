package com.hechi.niumall.filter.messageLoginFilter;

import com.alibaba.fastjson.JSONObject;
import com.hechi.niumall.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class EmailCodeAuthenticationFilter  extends AbstractAuthenticationProcessingFilter {


    /**
     * 前端传来的 参数名 - 用于request.getParameter 获取
     */
    private static final String DEFAULT_EMAIL_NAME="email";

    private static final String DEFAULT_EMAIL_CODE="e_code";

    private  String username=DEFAULT_EMAIL_NAME;

    private String password = DEFAULT_EMAIL_CODE;

    /**
     * 是否 仅仅post方式
     */
    private boolean postOnly = true;

    private  final String MESSAGE="message";
    @Autowired
    RedisCache redisCache;
    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    /**
     * 通过传入的参数创建匹配器
     * 即过滤 Filter 中的url
     */
 public  EmailCodeAuthenticationFilter(){
     super(new AntPathRequestMatcher("/login/message","POST"));
 }

    /**
     * filter 获得 用户名（邮箱/手机号）和密码（验证码） 装配到token 上，
     * 然后把token交给provider 进行授权
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
       if (postOnly && !"POST".equals(request.getMethod())) {
           throw new AuthenticationServiceException("授权请求不支持："+request.getMethod()+"  方式");
       }else {
           JSONObject jsonRequest = getJsonRequest(request);
           System.out.println("解释的参数： "+jsonRequest);
           String email= jsonRequest.getString(this.username);
           String code = jsonRequest.getString(this.password);
           code=(Objects.nonNull(code))? code : "";
           email=(Objects.nonNull(email))? email : "";
           email=email.trim();
           //如果 验证码不相等 故意让token出错 然后走 springsecurity 错误流程

           //todo 暂时不懂 封装 token
           EmailCodeAuthenticationToken token = checkCode(code,email)? new EmailCodeAuthenticationToken(email,new ArrayList<>()):new EmailCodeAuthenticationToken("error");
           this.setDetails(request,token);
           //交给 manager 发证
           return this.getAuthenticationManager().authenticate(token);
       }
    }
    /**
     * 获取 头部信息 让合适的provider 来验证他
     */
    public void setDetails(HttpServletRequest request , EmailCodeAuthenticationToken token ){
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    /**
     * 判断传来的验证码信息和服务器保存的验证码
     * @param code
     * @return
     */
    private boolean checkCode(String code,String email) {
        //获取储存在redis中的验证码进行比较
        String serviceCode = redisCache.getCacheObject(MESSAGE + email).toString();

        return Objects.nonNull(serviceCode) && serviceCode.equals(code);
    }

    /**
     * 获取post json 请求携带的数据转化为json 支付串
     * @param request
     * @return
     */
    private JSONObject getJsonRequest(HttpServletRequest request) {
        JSONObject result = null;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader();) {
            char[] buff = new char[1024];
            int len;
            while ((len = reader.read(buff)) != -1) {
                sb.append(buff, 0, len);
            }
            result = JSONObject.parseObject(sb.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
