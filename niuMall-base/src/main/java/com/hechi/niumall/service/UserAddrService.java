package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.UserAddr;
import com.hechi.niumall.result.ResponseResult;


/**
 * niumall商城-用户地址(UserAddr)表服务接口
 *
 * @author ccx
 * @since 2022-08-06 17:32:21
 */
public interface UserAddrService extends IService<UserAddr> {
    //    通过用户id获取地址
    ResponseResult getAddrByUser();

    ResponseResult getAddrByUserId(Long id);
    //  通过地址id 获取地址
    ResponseResult getAddrById(Long id);

    //创建新的用户地址
    ResponseResult createAddr(UserAddr addr);

    //更新用户 地址
    ResponseResult updateAddr(UserAddr addr);

    //通过id删除用户地址
    ResponseResult deleteAddrById(Long id);
}

