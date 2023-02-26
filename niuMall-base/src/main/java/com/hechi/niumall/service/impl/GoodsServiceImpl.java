package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.Goods;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.mapper.GoodsMapper;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.GoodsService;
import com.hechi.niumall.utils.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * niumall商城-商品(Goods)表服务实现类
 *
 * @author ccx
 * @since 2022-07-25 10:06:02
 */
//todo 商品下架后仍可以查询到
@Service("goodsService")
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Override
    /* 返回指定商品的详细信息
     * cId 品牌id
     * gId 商品id
     */
    public ResponseResult getGoodsDetils(Integer cId, Integer gId) {

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Goods::getBrandId, cId);
        lambdaQueryWrapper.eq(Goods::getId, gId);
        Goods one = getOne(lambdaQueryWrapper);
        return Objects.isNull(one)
                ? ResponseResult.errorResult(AppHttpCodeEnum.GOODS_IS_NOT_EXTST)
                : ResponseResult.okResult(one);

    }

    @Cacheable(value = "goods", key = "#id")
    @Override
    public ResponseResult getGoodsDetilsById(Integer id) {
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Goods::getId, id);
        Goods one = getOne(lambdaQueryWrapper);
        return ResponseResult.okResult(one);
    }

    @Override
    public ResponseResult getGoodsDetilsByKey(String key, Integer pages) {

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Goods::getIsShelves, 1).like(Goods::getTitle, key).or().like(Goods::getSimpleDesc, key);
        //todo 防止数据过多 可使用分页查询
        Page<Goods> page1 = new Page<>(pages, 20);
        page(page1, lambdaQueryWrapper);
        List<Goods> list = page1.getRecords();
        List<Goods> collect = list.stream()
                //去重
                .distinct()
                //过滤 有存货才能使用
                .filter(item -> item.getInventory() > 0)
                //排序
                .sorted((g1, g2) -> {
                    //排序按照销量 销量相等按照访问量
                    int i = g1.getSalesCount() - g2.getSalesCount();
                    int i1 = g1.getAccessCount() - g2.getAccessCount();
                    return i == 0 ? i1 : i;
                }).collect(Collectors.toList());
//        处理后的商品数据重新封装
        page1.setRecords(collect);
        if (collect.size() <= 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.GOODS_IS_NOT_EXTST);
        }
        return ResponseResult.okResult(page1);
    }

    @Override
    public ResponseResult getGoodsDetilsListByBrand(Integer cId, int pages) {
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Goods::getBrandId, cId).in(Goods::getBrandId, cId);
        Page<Goods> page1 = new Page<>(pages, 10);
        page(page1, lambdaQueryWrapper);
        List<Goods> records = page1.getRecords();
        //todo 待封装成包含分页信息的数据
        if (records.size() > 0) {
            return ResponseResult.okResult(page1);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.CATEGORY_GOODS_ISNULL);
    }


    @Override
    public ResponseResult getGoodsDetilsListByBrand(Integer[] cId, int pages) {
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Goods::getBrandId, (Object[]) cId);
        Page<Goods> page1 = new Page<>(pages, 20);
        page(page1, lambdaQueryWrapper);
        List<Goods> records = page1.getRecords();
        //todo 待封装成包含分页信息的数据
        if (records.size() > 0) {
            return ResponseResult.okResult(page1);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.CATEGORY_GOODS_ISNULL);
    }

    @Cacheable(value = "ListByCategory", key = "#cId+'L'+#pages")
    @Override
    public ResponseResult getGoodsDetilsListByCategory(Integer cId, int pages) {

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Goods::getCategory, cId);
        Page<Goods> page1 = new Page<>(pages, 20);
        page(page1, lambdaQueryWrapper);
        List<Goods> records = page1.getRecords();
        //封装成包含分页信息的数据
        if (records.size() > 0) {
            return ResponseResult.okResult(page1);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.CATEGORY_GOODS_NOT_EXIST);
    }

    @Cacheable(value = "mainPages", key = "'page'+#pages")
    @Override
    public ResponseResult getPages(int pages) {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(Goods::getCategory, 1, 6)
                .orderByDesc(Goods::getAccessCount);
        Page<Goods> page1 = new Page<>(pages, 50);
        page(page1, queryWrapper);
        //封装分页结果vo
        return ResponseResult.okResult(page1);
    }


    @Override
    public ResponseResult addGoods(Goods goods) {
        Long userId = SecurityUtils.getUserId();
        goods.setUpdateBy(userId);
        goods.setCreateBy(userId);
        goods.setCreateTime(new Date());
        goods.setUpdateTime(new Date());
        boolean save = save(goods);
        return save
                ? ResponseResult.okResult(goods)
                : ResponseResult.errorResult(AppHttpCodeEnum.GOODS_ADD_ERROR);
    }

    @CacheEvict(value = "goods", key = "#goods.id")
    @Override
    public ResponseResult updateGoods(Goods goods) {
        boolean b = updateById(goods);
        return b
                ? ResponseResult.okResult()
                : ResponseResult.errorResult(AppHttpCodeEnum.GOODS_UPDATE_ERROR);
    }

    @CacheEvict(value = "goods", key = "#goods.id")
    @Override
    public ResponseResult deleteGoods(Goods goods) {
        return this.deleteGoodsById(Long.valueOf(goods.getId()));
    }

    @CacheEvict(value = "goods", key = "#goods.id")
    @Override
    public ResponseResult deleteGoodsById(Long id) {
        boolean b = removeById(id);
        return b ? ResponseResult.okResult()
                : ResponseResult.errorResult(AppHttpCodeEnum.GOODS_DELEDTE_ERROR);
    }
}

