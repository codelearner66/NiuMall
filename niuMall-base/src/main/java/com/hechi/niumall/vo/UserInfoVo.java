package com.hechi.niumall.vo;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String avatar;
    private String sex;
    //账号余额
    private Long balance;
    private String phonenumber;
    private String email;
    //用户类型：0代表普通用户，1代表管理员
    private String type;

}