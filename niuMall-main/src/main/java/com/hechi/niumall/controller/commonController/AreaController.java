package com.hechi.niumall.controller.commonController;

import com.hechi.niumall.entity.Areadata;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.AreadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AreaController {

    @Autowired
    AreadataService areadataService;

    //    获取省信息
    @RequestMapping("/getProvince")
    public ResponseResult getProvince() {
        return areadataService.getProvince();
    }

    //    获取市信息
    @RequestMapping("/getCity")
    public ResponseResult getCity(Areadata province) {
        return areadataService.getCity(province);
    }

    //    获取县信息
    @RequestMapping("/getCounty")
    public ResponseResult getCounty(Areadata city) {
        return areadataService.getCounty(city);
    }

    //获取镇 街道信息
    @RequestMapping("/getTown")
    public ResponseResult getTown(Areadata county) {
        return areadataService.getTown(county);
    }

    //通过id查询地址
    @RequestMapping("/getAreadataById")
    public ResponseResult getAreadataById(Long id) {
        return areadataService.getAreadataById(id);
    }

}
