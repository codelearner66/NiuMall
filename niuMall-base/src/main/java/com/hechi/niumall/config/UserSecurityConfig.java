package com.hechi.niumall.config;

import com.hechi.niumall.filter.JwtAuthenticationTokenFilter;
import com.hechi.niumall.filter.messageLoginFilter.EmailCodeAuthenticationFilter;
import com.hechi.niumall.filter.messageLoginFilter.EmailCodeAuthenticationProvider;
import com.hechi.niumall.handler.securityHandler.MyAuthenticationFailureHandler;
import com.hechi.niumall.handler.securityHandler.MyAuthenticationSuccessHandler;
import com.hechi.niumall.service.SysUserService;
import com.hechi.niumall.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//开启注解鉴权
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    SysUserService userService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    //自定义认证成功处理器
   @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
//   自定义认证失败处理器
   @Autowired
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public EmailCodeAuthenticationFilter emailCodeAuthenticationFilter() {
        EmailCodeAuthenticationFilter emailCodeAuthenticationFilter = new EmailCodeAuthenticationFilter();
        emailCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        emailCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        return emailCodeAuthenticationFilter;
    }

    @Bean
    public EmailCodeAuthenticationProvider emailCodeAuthenticationProvider() {
        return new EmailCodeAuthenticationProvider(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String [] passUrl={"/login",
                "/logout",
                "/register",
                "/getCode",
                "/getMailCode",
                "/isexist",
                "/baseRegister",
                "/findpassword",
                "/getRootCategory",
                "/getChildCategory",
                "/getSlideShows",
                "/getPages",
                "/getModifyNotification",
                "/pay/api/trade/notify",
                "/socket/api/messageService/**"};
        // 定制请求的授权规则 //关闭csrf
        http.csrf().disable()
                //关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(passUrl).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and().authenticationProvider(emailCodeAuthenticationProvider())
                //把token校验过滤器添加到过滤器链中
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(emailCodeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        //允许跨域
        http.cors()
        //关闭默认的注销功能
        .and().logout().disable();
        //配置自定义的异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(emailCodeAuthenticationProvider());
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
