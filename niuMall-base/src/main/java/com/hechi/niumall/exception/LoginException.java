package com.hechi.niumall.exception;

import com.hechi.niumall.enums.AppHttpCodeEnum;

/**
 * @author ccx
 *
 * 在登陆时出现的异常
 */
public class LoginException extends RuntimeException{
    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public LoginException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
