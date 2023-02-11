package com.hechi.niumall.controller.commodity;

import com.hechi.niumall.entity.GoodsCategory;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GetCategoryNav {
    @Autowired
    GoodsCategoryService goodsCategoryService;

    @RequestMapping("/getRootCategory")
    public ResponseResult getRootCategory() {
        return goodsCategoryService.getRootCategory();
    }

    @RequestMapping("/getAdminRootCategory")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult getAdminRootCategory() {
        return goodsCategoryService.getAdminRootCategory();
    }


    @RequestMapping("/getAdminGoodsCategoryById")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult getAdminGoodsCategoryById(Integer id) {
        return goodsCategoryService.getAdminGoodsCategoryById(id);
    }


    /**
     * 获取所有类别
     *
     * @return
     */
    @RequestMapping("/getCategory")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult getCategory(int pages) {
        return goodsCategoryService.getGoodsCategory(pages);
    }

    @RequestMapping("/getChildCategory")
    public ResponseResult getChildCategory(Integer id) {
        return goodsCategoryService.getGoodsCategoryById(id);
    }

    /**
     * 更新分类
     *
     * @param category
     * @return
     */
    @PostMapping("/updataCategory")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updataCategory(@RequestBody GoodsCategory category) {
        return goodsCategoryService.updateGoodsCategorById(category);
    }

    /**
     * 删除分类
     *
     * @param category
     * @return
     */
    @PostMapping("/deleteGoodsCategory")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult deleteGoodsCategory(@RequestBody GoodsCategory category) {
        return goodsCategoryService.deleteGoodsCategory(category);
    }

    /**
     * 添加新的分类
     *
     * @param category 分类信息
     * @return
     */
    @RequestMapping("addCategory")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult addCategory(@RequestBody GoodsCategory category) {
        return goodsCategoryService.addGoodsCategory(category);
    }

}
