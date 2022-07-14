package com.hechi.niumall.controller.Login;

import com.alibaba.fastjson.JSONObject;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.LoginService;
import com.hechi.niumall.service.MessageService;
import com.hechi.niumall.service.SysUserService;
import com.hechi.niumall.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;

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
    MessageService messageService;
    @Autowired
    LoginService loginService;
     @Autowired
     SysUserService sysUserService;
    @Autowired
    MailUtils mailUtils;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody SysUser sysUser){
        return loginService.login(sysUser);
    }

    /**
     * 获取手机验证码
     * @param phoneNumber
     * @return
     * @throws Exception
     */
    @PostMapping("/getCode")
    public ResponseResult getCode(@RequestBody String phoneNumber) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(phoneNumber);
        System.out.println("手机号为： "+jsonObject.get("phoneNumber").toString());
        messageService.sentMessage(jsonObject.get("phoneNumber").toString());
        return ResponseResult.okResult();
    }
    /**
     * 获取邮箱验证码
     * @param phoneNumber
     * @return
     * @throws Exception
     */
    @PostMapping("/getMailCode")
    public ResponseResult getMailCode(@RequestBody String phoneNumber) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(phoneNumber);
        System.out.println("邮箱验证码：： "+jsonObject.get("phoneNumber").toString());
        messageService.sentMailCode(jsonObject.get("phoneNumber").toString());
        return ResponseResult.okResult();
    }
    /**
     * 登出
     * @return
     */
    @RequestMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
    /**
     * 找回密码
     */
    @PostMapping("/findpassword")
    public ResponseResult findpassword(@NonNull @RequestBody SysUser user) throws Exception {
       return sysUserService.findpassword(user);
    }
    /**
     * 判断帐号是否存在
     * @param user
     * @return
     */
    @RequestMapping("/isexist")
    public ResponseResult isExist(@RequestBody @Valid @NonNull SysUser user){
      return   sysUserService.isExist(user);
    }

    /**
     * 测试发邮件
     * @throws MessagingException
     */
    @RequestMapping("/mail")
    public void testMail() throws MessagingException {
//        try {
//            EmailParam emailParam = new EmailParam();
//            emailParam.setStuName("张阿牛");
//            emailParam.setItemName("亚太银行账目统计");
//            emailParam.setUpdateContent("付款到账");
//            emailParam.setUpdatePerson("盖茨");
//            emailParam.setRemarks("成功到账");
//            //此处to数组输入多个值，即可实现批量发送
            String [] to={"553378997@qq.com"};
//            mailUtils.thymeleafEmail(to, "这是一封测试邮件主题", emailParam, "mailTest", "static/images/01.jpg");
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
       // mailUtils.sentMailCode(to[0],"123456");
    }
}
