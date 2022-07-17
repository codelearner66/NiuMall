package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.GoodsCategory;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.mapper.GoodsCategoryMapper;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.GoodsCategoryService;
import com.hechi.niumall.utils.BeanCopyUtils;
import com.hechi.niumall.utils.RedisCache;
import com.hechi.niumall.vo.GoodsCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * niumall商城-商品分类(GoodsCategory)表服务实现类
 *
 * @author ccx
 * @since 2022-07-14 21:48:55
 */
@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {
    //todo 将类别信息存入redis提高查询效率
    //定义商品分类统一前缀
    private  static  final  String PREFIX = "CATEGORY";

    @Autowired
    RedisCache redisCache;
    //查询根类别
    @Override
    public ResponseResult getRootCategory() {
        //先去redis中去取 取不到再去查数据库
        List<GoodsCategory> cacheList =redisCache.getCacheList(PREFIX + ":ROOT");
        if(Objects.isNull(cacheList)||cacheList.size() == 0){
            LambdaQueryWrapper<GoodsCategory> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(GoodsCategory ::getPid,-1);
            cacheList = list(queryWrapper);
            //如果是查询的数据库 就将查询的结果再次写道redis中
            redisCache.setCacheList(PREFIX + ":ROOT",cacheList);
        }
        List<GoodsCategoryVo> goodsCategoryVos = BeanCopyUtils.copyBeanList(cacheList, GoodsCategoryVo.class);
        return ResponseResult.okResult(goodsCategoryVos);
    }

    /**
     * 根据根类别查找子品牌
     * @param id
     * @return
     */
    @Override
    public ResponseResult getGoodsCategoryById(int id) {

        List<GoodsCategory> cacheList = redisCache.getCacheList(PREFIX + ":" + id);
         if (cacheList == null||cacheList.size() == 0) {
             LambdaQueryWrapper<GoodsCategory> queryWrapper=new LambdaQueryWrapper<>();
             queryWrapper.eq(GoodsCategory ::getPid,id);
             cacheList = list(queryWrapper);
             redisCache.setCacheList(PREFIX + ":"+id,cacheList);
         }
        List<GoodsCategoryVo> goodsCategoryVos = BeanCopyUtils.copyBeanList(cacheList, GoodsCategoryVo.class);
        return ResponseResult.okResult(goodsCategoryVos);
    }

    /**
     * 添加新的类别
     */
    @Override
    public ResponseResult addGoodsCategory(GoodsCategory category) {
        if(save(category)){
            if (category.getPid()==-1) {
                LambdaQueryWrapper<GoodsCategory> queryWrapper=new LambdaQueryWrapper<>();
                queryWrapper.eq(GoodsCategory ::getPid,-1);
                //将查询的结果再次写道redis中
                redisCache.setCacheList(PREFIX + ":ROOT", list(queryWrapper));
            }else {
                LambdaQueryWrapper<GoodsCategory> queryWrapper=new LambdaQueryWrapper<>();
                queryWrapper.eq(GoodsCategory ::getPid,category.getPid());
                redisCache.setCacheList(PREFIX + ":"+category.getPid(),list(queryWrapper));
            }
            return  ResponseResult.okResult();
        }
        else {
            return ResponseResult.errorResult(AppHttpCodeEnum.ADD_CATEGORY_ERROR);
        }
    }

    /**
     * 根据id 更新类别信息
     * @param category
     * @return
     */
    @Override
    public ResponseResult updateGoodsCategorById(GoodsCategory category) {
        GoodsCategory byId = getById(category.getId());
        if (byId == null) {
            return  ResponseResult.errorResult(AppHttpCodeEnum.CATEGORY_ERROR);
        }
        else {
            LambdaUpdateWrapper<GoodsCategory> updateWarpper = new LambdaUpdateWrapper<>();
            updateWarpper.eq(GoodsCategory ::getId,category.getId());
            if(update(category, updateWarpper)){
                LambdaQueryWrapper<GoodsCategory> queryWrapper=new LambdaQueryWrapper<>();
                queryWrapper.eq(GoodsCategory ::getPid,category.getPid());
                redisCache.setCacheList(PREFIX + ":"+category.getPid(),list(queryWrapper));
                return  ResponseResult.okResult();
            }
            else {
                return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_CATEGORY_ERROR);
            }
        }
    }
}
