package com.hechi.niumall.controller.goods;

import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.GoodsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/getGoodsDetils/{cId}/{gId}")
    public ResponseResult getGoodsDetils(@PathVariable("cId") Integer cId,@PathVariable("gId") Integer gId){
        System.out.println(cId+"   "+gId);
        return goodsService.getGoodsDetils(cId, gId);
    }

    @RequestMapping("/getGoodsDetilsById")
   public ResponseResult getGoodsDetilsById(Integer id){
      return   goodsService.getGoodsDetilsById(id);
    }

    @RequestMapping("/getGoodsDetilsByKey")
    ResponseResult getGoodsDetilsByKey(@NotNull String key,Integer pages){
        return goodsService.getGoodsDetilsByKey(key,pages);
    }

    @RequestMapping("/getGoodsDetilsListByBrand")
    ResponseResult getGoodsDetilsListByBrand( Integer[] cId,  Integer pages){
        System.out.println(Arrays.toString(cId));
        return goodsService.getGoodsDetilsListByBrand(cId, pages);
    }

    @RequestMapping("/getGoodsDetilsListByCategory")
    ResponseResult getGoodsDetilsListByCategory(Integer cId,int pages){
        return goodsService.getGoodsDetilsListByCategory(cId, pages);
    }

    @RequestMapping("/getPages")
    ResponseResult getPages(int pages){
        return goodsService.getPages(pages);
    }
}
