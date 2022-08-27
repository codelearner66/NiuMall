package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.Goods;
import com.hechi.niumall.result.ResponseResult;


/**
 * niumall商城-商品(Goods)表服务接口
 *
 * @author ccx
 * @since 2022-07-25 10:06:01
 */
public interface GoodsService extends IService<Goods> {
    /**
     * 通过品牌id和商品id返回商品信息
     * @param cId
     * @param gId
     * @return
     */
    ResponseResult getGoodsDetils(Integer cId, Integer gId);

    /**
     * 根据商品id 获取商品详情
     * @param id
     * @return
     */
    ResponseResult getGoodsDetilsById(Integer id);

    /**
     * 通过关键词搜索商品列表
     * @param key
     * @return
     */
    ResponseResult getGoodsDetilsByKey(String key,Integer pages);

    /**
     * 通过品牌获取商品列表
     * @param cId  品牌id
     * pages 分页参数
     * @return
     */
    ResponseResult getGoodsDetilsListByBrand(Integer cId,int pages);
    ResponseResult getGoodsDetilsListByBrand(Integer []cId,int pages);

    /**
     * 根据分类获取商品列表
     * @param cId 分类id
     * @param pages 分页参数
     * @return
     */
    ResponseResult getGoodsDetilsListByCategory(Integer cId,int pages);

    /**
     * 随机聚合推送商品
     * @param pages 品牌id
     * @return  包含分页信息
     */
    ResponseResult getPages(int pages);


    ResponseResult addGoods(Goods goods);

    /**
     * 更新商品
     * @param goods 商品信息
     * @return
     */
    ResponseResult updateGoods(Goods goods);

    /**
     * 删除商品
     * @param goods 商品信息
     * @return
     */
    ResponseResult deleteGoods(Goods goods);

    /**
     * 通过id删除商品
     * @param id 商品id
     * @return
     */
    ResponseResult deleteGoodsById(Long id);
}

