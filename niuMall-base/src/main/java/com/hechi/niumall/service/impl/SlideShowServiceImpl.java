package com.hechi.niumall.service.impl;

import com.hechi.niumall.entity.SlideShowItem;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SlideShowService;
import com.hechi.niumall.utils.RedisCache;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.utils.TxyunUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 轮播图信息储存在redis中
 * 所以对它的操作也是基于redis
 *
 * @return
 */
@Service
@Slf4j
public class SlideShowServiceImpl implements SlideShowService {

    private static final String PREFIX = "SLIDE";

    @Autowired
    RedisCache redisCache;
    @Autowired
    TxyunUtils txyunUtils;


    @Override
    public ResponseResult getSlideShow() {
        List<SlideShowItem> cacheList = redisCache.getCacheList(PREFIX);
        if (cacheList != null && cacheList.size() > 0) {
            try {
                Authentication authentication = SecurityUtils.getAuthentication();
                if (authentication != null && authentication.isAuthenticated() && SecurityUtils.isAdmin()) {
                    return ResponseResult.okResult(cacheList);
                }
            } catch (Exception e) {
                log.error("发生错误：{}", e.getMessage());
            }
            List<SlideShowItem> collect = cacheList.stream()
                    .filter(showItem -> showItem.getIsEnable() != 1)
                    .collect(Collectors.toList());
            return ResponseResult.okResult(collect);
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_IS_NULL);
        }
    }

    @Override
    @Transactional
    public ResponseResult deleteSlideShow(Integer id) {
        List<SlideShowItem> slideList = redisCache.getCacheList(PREFIX);
        if (slideList != null && slideList.size() > 0) {
            for (int i = 0; i < slideList.size(); i++) {
                //过滤掉id相等的项
                if (slideList.get(i).getId().equals(id)) {
                    SlideShowItem remove = slideList.remove(i);
                    //云端图片也要删除
                    deleteSlideShowImages(remove.getImageUrl());
                    break;
                }
            }
            //更新缓存数据
            return update(slideList)
                    ? ResponseResult.okResult()
                    : ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_UPDATA_ERROE);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_IS_NULL);
    }

    @Override
    @Transactional
    public ResponseResult deleteAllSlideShow() {
        List<SlideShowItem> slideList = redisCache.getCacheList(PREFIX);
        if (slideList != null && slideList.size() > 0) {
            for (SlideShowItem slideShowItem : slideList) {
                deleteSlideShowImages(slideShowItem.getImageUrl());
            }
        }
        boolean update = update(null);
        return update
                ? ResponseResult.okResult()
                : ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_UPDATA_ERROE);
    }

    @Override
    public ResponseResult updateSlideShow(Integer id, SlideShowItem showItem) {
        List<SlideShowItem> cacheList = redisCache.getCacheList(PREFIX);
        if (cacheList != null && cacheList.size() > 0) {
            for (SlideShowItem item : cacheList) {
                if (item.getId().equals(id)) {
                    item.setGId(item.getGId().equals(showItem.getGId()) || Objects.isNull(showItem.getGId()) ? item.getGId() : showItem.getGId());
                    item.setDesc(item.getDesc().equals(showItem.getDesc()) || Objects.isNull(showItem.getDesc()) ? item.getDesc() : showItem.getDesc());
                    boolean b = (!item.getImageUrl().equals(showItem.getImageUrl())) && Objects.nonNull(showItem.getImageUrl());
                    if (b) {
                        // 删除图片
                        deleteSlideShowImages(item.getImageUrl());
                    }
                    item.setImageUrl(!b ? item.getImageUrl() : showItem.getImageUrl());
                    item.setIsEnable(item.getIsEnable().equals(showItem.getIsEnable()) || Objects.isNull(showItem.getIsEnable()) ? item.getIsEnable() : showItem.getIsEnable());
                    item.setUpdateTime(new Date());
                    item.setUpdatedBy(SecurityUtils.getLoginUser().getUsername());
                }
            }
            update(cacheList);
        }
        return ResponseResult.okResult();
    }


    @Override
    @Transactional
    public ResponseResult addSlideShow(SlideShowItem showItem) {
        List<SlideShowItem> cacheList = redisCache.getCacheList(PREFIX);
        if (cacheList != null) {
            Random random = new Random(System.currentTimeMillis());
            int i = random.nextInt() * 1000;
            showItem.setId(cacheList.size() * i % 1000);
            showItem.setCreateTime(new Date());
            showItem.setCreatedBy(SecurityUtils.getLoginUser().getUsername());
            cacheList.add(showItem);
        }
        return update(cacheList)
                ? ResponseResult.okResult()
                : ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_UPDATA_ERROE);
    }

    @Override
    public ResponseResult updateAllSlideShow(List<SlideShowItem> list) {
        return update(list)
                ? ResponseResult.okResult()
                : ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_UPDATA_ERROE);
    }

    @Override
    public ResponseResult addSlideShowImages(MultipartFile file) throws IOException {
        String s = txyunUtils.doUpdata(file, "/slideshow/");
        return ResponseResult.okResult(s);
    }

    @Override
    public ResponseResult deleteSlideShowImages(String fileName) {
        String pre = "https://miumall-1306251195.cos.ap-chengdu.myqcloud.com/";
        String replace = fileName.replace(pre, "");
        txyunUtils.doDelete(replace);
        return ResponseResult.okResult();
    }

    private boolean update(List<SlideShowItem> list) {
        if (Objects.isNull(list) || list.size() == 0) {
            return redisCache.deleteObject(PREFIX);
        }
//        如果传递的值不为空 先删除后添加
        redisCache.deleteObject(PREFIX);
        return redisCache.setCacheList(PREFIX, list) > 0;
    }
}
