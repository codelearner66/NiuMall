package com.hechi.niumall.controller.commodity;

import com.hechi.niumall.entity.GoodsCategory;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GetCategoryNav {
    @Autowired
    GoodsCategoryService goodsCategoryService;

    @RequestMapping("/getRootCategory")
    public ResponseResult getRootCategory(){
        return goodsCategoryService.getRootCategory();
    }
    @RequestMapping("/getChildCategory")
    public ResponseResult getChildCategory(Integer id){
        return goodsCategoryService.getGoodsCategoryById(id);
    }

    /**
     * 添加新的分类
     * @param category 分类信息
     * @return
     */
    @RequestMapping("addCategory")
    public ResponseResult addCategory(GoodsCategory category){
      return   goodsCategoryService.addGoodsCategory(category);
    }

}
