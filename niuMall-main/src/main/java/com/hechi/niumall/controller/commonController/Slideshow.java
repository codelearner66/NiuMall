package com.hechi.niumall.controller.commonController;

import com.hechi.niumall.entity.SlideShowItem;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SlideShowService;
import com.hechi.niumall.utils.TxyunUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@Slf4j
public class Slideshow {
    @Autowired
    TxyunUtils txyunUtils;
    @Autowired
    SlideShowService slideShowService;


    /**
     * 获取轮播图图片数据
     *
     * @return
     */
    @RequestMapping("/getSlideShows")
    public ResponseResult getSlideShow() {
        return slideShowService.getSlideShow();
    }

    /**
     * 更新轮播图
     *
     * @param showItem 轮播图对象
     * @return
     */
    @PostMapping("/updataSlideShows")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updataSlideShows(@RequestBody SlideShowItem showItem) {
        log.info("进入更新：{}",showItem);
        return slideShowService.updateSlideShow(showItem.getId(), showItem);
    }

    @PostMapping("/addSlideShows")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult addSlideShows(@RequestBody SlideShowItem showItem) {
        return slideShowService.addSlideShow(showItem);
    }

    @RequestMapping("/deleteSlideShows/{id}")
    public ResponseResult deleteSlideShows(@PathVariable("id") Integer id) {
        return slideShowService.deleteSlideShow(id);
    }

    /**
     * 上传轮播图图片
     *
     * @param file 图片资源
     * @return
     * @throws IOException 文件替换时可能会发生io异常
     */
    @PostMapping("/uploadSlideShowImages")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult uploadSlideShowImages(@RequestParam("file") MultipartFile[] file,@RequestParam("imageUrl") String  showItem) throws IOException {
        if (file != null) {
            if (Objects.nonNull(showItem)&&!"-1".equals(showItem)){
                slideShowService.deleteSlideShowImages(showItem);
            }
            return slideShowService.addSlideShowImages(file[0]);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SLIDESHOW_UPDATA_ERROE);
    }

    /**
     * 删除轮播图图片
     *
     * @param fileName 图片路径
     * @return
     */
    @RequestMapping("/deleteSlideShowImages")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    private ResponseResult deleteSlideShowImages(@RequestParam("fileName") String fileName) {
        return slideShowService.deleteSlideShowImages(fileName);
    }
}
