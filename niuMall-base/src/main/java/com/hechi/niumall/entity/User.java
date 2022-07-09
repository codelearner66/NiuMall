package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 *
 * @author ccx
 * @since 2022-07-09 14:20:11
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User  {
    @TableId
    private Long id;

    //用户名
    private String username;
    //姓名
    private String nickname;
    //密码，加密存储
    private String password;
    //注册手机号
    private String phone;
    //注册邮箱
    private String email;
    //帐号状态
    private String status;
    //工作
    private String job;
    
    private Date created;
    
    private Date updated;



}

