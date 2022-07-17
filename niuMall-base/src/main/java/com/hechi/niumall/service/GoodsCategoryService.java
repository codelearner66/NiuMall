package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.GoodsCategory;
import com.hechi.niumall.result.ResponseResult;


/**
 * niumall商城-商品分类(GoodsCategory)表服务接口
 *
 * @author ccx
 * @since 2022-07-14 21:48:55
 */
public interface GoodsCategoryService extends IService<GoodsCategory> {
    /**
     * 查询根类别
     * @return
     */
   public   ResponseResult getRootCategory();

    /**
     * 通过根类别查询子类
     * @param id
     * @return
     */
   public  ResponseResult getGoodsCategoryById(int id);

    /**
     * 添加新的类别
     * @param category
     * @return
     */
   public  ResponseResult addGoodsCategory(GoodsCategory category);

    /**
     * 通过id 更新类别
     */
    public  ResponseResult updateGoodsCategorById(GoodsCategory category);
}

