package com.hechi.niumall.handler.exception;

import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.exception.LoginException;
import com.hechi.niumall.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseResult systemExceptionHandler(LoginException e) {
        //打印异常信息
        log.error("出现了异常！ {}", e.getMsg());
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseResult internalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        //打印异常信息
        log.error("出现了异常！ {}", e.getMessage());
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(500, e.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseResult disabledExceptionHandler(DisabledException e) {
        log.error("账号异常！{}", e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
   @ExceptionHandler(IllegalStateException.class)
   public ResponseResult IllegalStateExceptionHandle(IllegalStateException e){
       log.error("IllegalStateExceptionHandle异常！{}", e.getMessage());
       return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
   }
    @ExceptionHandler(LockedException.class)
    public ResponseResult lockedExceptionHAndler(LockedException e) {
        log.error("账号被锁定！{}", e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.ACCOUNT_LOCKED);
    }
//@ExceptionHandler(Meth)
    //全局异常处理 开启后屏蔽所有的异常 开发时不推荐使用
//    @ExceptionHandler(Exception.class)
//    public ResponseResult exceptionHandler(Exception e){
//        //打印异常信息
//        log.error("出现了异常！ {}",e.getMessage());
//        //从异常对象中获取提示信息封装返回
//        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
//    }
}