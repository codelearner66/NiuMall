package com.hechi.niumall.service.impl;

import com.hechi.niumall.entity.SlideShowItem;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SlideShowService;
import com.hechi.niumall.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 轮播图信息储存在redis中
 * 所以对它的操作也是基于redis
 * @return
 */
@Service
public class SlideShowServiceImpl implements SlideShowService {

    private  static final String PREFIX = "SLIDE";

    @Autowired
    RedisCache redisCache;


    @Override
    public ResponseResult getSlideShow() {
        List<SlideShowItem> cacheList = redisCache.getCacheList(PREFIX);
        if (cacheList!=null&&cacheList.size() > 0) {
            return ResponseResult.okResult(cacheList);
        }
        else {
            return ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_IS_NULL);
        }
    }

    @Override
    public ResponseResult deleteSlideShow(Integer id) {
        List<SlideShowItem> slideList = redisCache.getCacheList(PREFIX);
        List<SlideShowItem> collect=null;
        if (slideList != null && slideList.size() > 0) {
            //过滤掉id相等的项
            //todo 云端图片也要删除
           collect = slideList.stream()
                    .filter(showItem -> !showItem.getId().equals(id))
                    .collect(Collectors.toList());
           //更新缓存数据
           update(collect);
        }
        return ResponseResult.okResult(collect);
    }

    @Override
    public ResponseResult deleteAllSlideShow() {
        boolean update = update(null);
        return update
                ?  ResponseResult.okResult()
                :  ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_UPDATA_ERROE);
    }

    @Override
    public ResponseResult updateSlideShow(Integer id, SlideShowItem showItem) {
        List<SlideShowItem> cacheList = redisCache.getCacheList(PREFIX);
        if (cacheList != null && cacheList.size() > 0) {
            for (SlideShowItem item : cacheList) {
                if (item.getId().equals(id)) {
                    item.setGId(showItem.getGId());
                    item.setImageUrl(showItem.getImageUrl());
                }
            }
            update(cacheList);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateAllSlideShow(List<SlideShowItem> list) {
        return update(list)
                ? ResponseResult.okResult()
                : ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_UPDATA_ERROE);
    }
    private  boolean update(List<SlideShowItem> list){
        Boolean delete = redisCache.redisTemplate.delete(PREFIX);
        return  redisCache.setCacheList(PREFIX, list)>0;
    }
}
