package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.Areadata;
import com.hechi.niumall.result.ResponseResult;


/**
 * (Areadata)表服务接口
 *
 * @author ccx
 * @since 2022-08-03 23:54:10
 */
public interface AreadataService extends IService<Areadata> {
    //    获取省信息
    ResponseResult getProvince();

    //    获取市信息
    ResponseResult getCity(Areadata province);

    //    获取县信息
    ResponseResult  getCounty(Areadata city);

    //    获取镇 街道信息
    ResponseResult  getTown(Areadata county);

    //通过id 获取信息
    ResponseResult getAreadataById(Long id);
}

