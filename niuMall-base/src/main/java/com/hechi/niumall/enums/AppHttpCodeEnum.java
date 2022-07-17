package com.hechi.niumall.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    CONTENT_NOT_NULL(506, "评论内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传png文件"),
    FILE_SIZE_ERROR(508,"文件大小超过限制"),

    LOGIN_TIMEOUT(1001,"说明登录过期  提示重新登录"),
    ACCOUNT_LOCKED(1002,"被锁定或封禁 提示重新登录"),
    LOGIN_ERROR(1003,"用户名或密码错误"),
    LOGIN_TOKEN_ERROR(1004,"用户令牌异常 提示重新登录"),


    ADD_CATEGORY_ERROR(2000,"添加新的类别出错"),
    CATEGORY_ERROR(2001,"商品类别不存在"),
    UPDATE_CATEGORY_ERROR(2002,"更新商品类别出错");


    int code;
    String msg;
    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}