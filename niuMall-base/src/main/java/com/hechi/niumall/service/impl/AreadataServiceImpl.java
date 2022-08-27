package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.Areadata;
import com.hechi.niumall.mapper.AreadataMapper;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.AreadataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Areadata)表服务实现类
 *
 * @author ccx
 * @since 2022-08-03 23:54:10
 */
@Service("areadataService")
public class AreadataServiceImpl extends ServiceImpl<AreadataMapper, Areadata> implements AreadataService {
    @Override
    public ResponseResult getProvince() {
         Areadata areadata =new Areadata();
         areadata.setId(-1L);
        return getData(0,areadata);
    }
    @Override
    public ResponseResult getCity(Areadata province) {

        return getData(1,province);
    }

    @Override
    public ResponseResult getCounty(Areadata city) {
        return getData(2,city);
    }

    @Override
    public ResponseResult getTown(Areadata county) {
        return getData(3,county);
    }

    @Override
    public ResponseResult getAreadataById(Long id) {
        LambdaQueryWrapper<Areadata> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Areadata::getId,id);
        Areadata one = getOne(lambdaQueryWrapper);
        return ResponseResult.okResult(one);
    }

    private ResponseResult getData(Integer deep,Areadata province){
        LambdaQueryWrapper<Areadata> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Areadata ::getDeep,deep).eq(province.getId()!=-1,Areadata ::getPid,province.getId());
        List<Areadata> list = list(lambdaQueryWrapper);
        return ResponseResult.okResult(list);
    }
}
