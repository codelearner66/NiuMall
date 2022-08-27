package com.hechi.niumall.controller.commonController;

import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SlideShowService;
import com.hechi.niumall.utils.TxyunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Slideshow {
    @Autowired
    TxyunUtils txyunUtils;
    @Autowired
    SlideShowService slideShowService;


    /**
     * 获取轮播图图片数据
     * @return
     */
    @RequestMapping("/getSlideShows")
    public ResponseResult getSlideShow(){
//        List<String> urlList = txyunUtils.getUrlList("slideshow/");
//        List<SlideShowItem> list = new ArrayList<>();
//        Random random= new Random();
//        int i=1;
//        for (String url : urlList) {
//            SlideShowItem item = new SlideShowItem();
//            item.setId(i++);
//            item.setGId(String.valueOf(random.nextInt(1000)));
//            item.setImageUrl(url);
//            list.add(item);
//        }
//        slideShowService.updateAllSlideShow(list);
        return slideShowService.getSlideShow();
    }
    /**
     * 对轮播图的管理配合文件上传
     */
//    @PostMapping("/updataSlideShows")
//    public ResponseResult  updataSlideShows(){
//
//    }
}
