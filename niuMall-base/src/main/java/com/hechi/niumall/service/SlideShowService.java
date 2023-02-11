package com.hechi.niumall.service;

import com.hechi.niumall.entity.SlideShowItem;
import com.hechi.niumall.result.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 对轮播图进行操作
 * @author ccx
 */
public interface SlideShowService {
    /**
     * 获取轮播图信息
     * @return
     */
    ResponseResult getSlideShow();

    /**
     * 根据id删除指定轮播图
     * @param id
     * @return
     */
    ResponseResult deleteSlideShow(Integer id);

    /**
     * 删除所有轮播图
     * @return
     */
    ResponseResult deleteAllSlideShow();

    /**
     * 根据id更新轮指定播图
     * @param id
     * @param showItem
     * @return
     */
    ResponseResult updateSlideShow(Integer id,SlideShowItem showItem);


    ResponseResult addSlideShow(SlideShowItem showItem);

    /**
     * 更新所有轮播图
     * 删除之前的轮播图
     * @param list
     * @return
     */
    ResponseResult updateAllSlideShow(List<SlideShowItem> list);

    ResponseResult addSlideShowImages(MultipartFile file) throws IOException;

    ResponseResult deleteSlideShowImages(String fileName);
}
