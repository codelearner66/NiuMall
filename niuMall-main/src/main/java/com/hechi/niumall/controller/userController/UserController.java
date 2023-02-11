package com.hechi.niumall.controller.userController;

import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SysUserService;
import com.hechi.niumall.utils.BeanCopyUtils;
import com.hechi.niumall.utils.TxyunUtils;
import com.hechi.niumall.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class UserController {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    TxyunUtils txyunUtils;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping("/getUserInfor")
    public ResponseResult getUserInfor() {
        return sysUserService.getUser();
    }

    @PostMapping("/checkPassword")
    public ResponseResult checkPassword(@RequestBody SysUser sysUser) {
        return sysUserService.checkPassword(sysUser.getPassword());
    }
    @RequestMapping("/uploadHeader")
    public ResponseResult uploadHeader(MultipartFile file) throws IOException {
        // 修改图片文件后 删之前的图片 并更新数据库
        String s =sysUserService.uploadHeader(file);

        return ResponseResult.okResult(s);
    }
    
    @PostMapping("/updataUser")
    public ResponseResult updataUser(@RequestBody @Valid UserInfoVo sysUser){
        SysUser sysUser1 = BeanCopyUtils.copyBean(sysUser, SysUser.class);
        boolean b = sysUserService.updataUser(sysUser1);
      return b?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
    @PostMapping("/updataPassword")
    public ResponseResult updataPassword(@RequestBody SysUser sysuser){
        if (sysuser.getId()!=null&&sysuser.getPassword() != null)
        {
            sysuser.setPassword(passwordEncoder.encode(sysuser.getPassword()));
            boolean b = sysUserService.updataUser(sysuser);
            return b?ResponseResult.okResult():ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        else {
            return  ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }


    }

}
