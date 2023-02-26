package com.hechi.niumall.controller.goods;

import com.hechi.niumall.entity.Goods;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.GoodsService;
import com.hechi.niumall.utils.TxyunUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

@RestController
public class GoodsController {
    static HashMap<String, String> map = new HashMap<>();
    @Autowired
    GoodsService goodsService;
    @Autowired
    TxyunUtils txyunUtils;
    {
        map.put("1", "phone/");
        map.put("2", "computer/");
        map.put("3", "gamePlay/");
        map.put("4", "uva/");
        map.put("5", "headset/");
        map.put("6", "camera/");
    }

    @RequestMapping("/getGoodsDetils/{cId}/{gId}")
    public ResponseResult getGoodsDetils(@PathVariable("cId") Integer cId, @PathVariable("gId") Integer gId) {
        System.out.println(cId + "   " + gId);
        return goodsService.getGoodsDetils(cId, gId);
    }

    //    商品总数
    @GetMapping("/goodsCount")
    public ResponseResult getGoodsCounts() {
        return ResponseResult.okResult(goodsService.count());
    }

    @RequestMapping("/getGoodsDetilsById")
    public ResponseResult getGoodsDetilsById(Integer id) {
        return goodsService.getGoodsDetilsById(id);
    }

    @RequestMapping("/getGoodsDetilsByKey")
    ResponseResult getGoodsDetilsByKey(@NotNull String key, Integer pages) {
        return goodsService.getGoodsDetilsByKey(key, pages);
    }

    @RequestMapping("/getGoodsDetilsListByBrand")
    ResponseResult getGoodsDetilsListByBrand(Integer[] cId, Integer pages) {
        System.out.println(Arrays.toString(cId));
        return goodsService.getGoodsDetilsListByBrand(cId, pages);
    }

    @RequestMapping("/getGoodsDetilsListByCategory")
    ResponseResult getGoodsDetilsListByCategory(Integer cId, int pages) {
        return goodsService.getGoodsDetilsListByCategory(cId, pages);
    }

    @RequestMapping("/getPages")
    ResponseResult getPages(int pages) {
        return goodsService.getPages(pages);
    }

    @RequestMapping("/addGoods")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult addGoods(@RequestBody Goods goods){
        System.out.println("接收到的数据：  "+goods);
        goods.setIsExistManySpec(0);
        goods.setSalesCount(0);
        goods.setAccessCount(0);
        return goodsService.addGoods(goods);
    }

     // 修改商品信息
    @RequestMapping("/modifyGoods")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult modifyGoods(@RequestBody Goods goods){
        return goodsService.updateGoods(goods);
    }

    //    上传商品首页展示图片
    @RequestMapping("/uploadGoodsImages")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult uploadGoodsImages(@RequestParam("file") MultipartFile file, @RequestParam("0") String category) throws IOException {
        System.out.println(file.getOriginalFilename() + "   " + category);
        String fileName = "";
        if (!Objects.isNull(category)) {
            fileName = "/images/" + map.get(category);
        }
        String s = txyunUtils.doUpdata(file, fileName);
        return ResponseResult.okResult(s);
    }

    @RequestMapping("/uploadGoodsPhotoAlbum")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult uploadGoodsPhotoAlbum(@RequestParam("file") MultipartFile[] file, @RequestParam("0") String category) throws IOException {
        System.out.println(file.length + "   " + category);
        String[] fileName = null;
        if (file.length > 0 && !Objects.isNull(category)) {
            fileName = new String[file.length];
            for (int i = 0; i < file.length; i++) {
                fileName[i] = txyunUtils.doUpdata(file[i], "/images/" + map.get(category));
            }
        }
        return ResponseResult.okResult(fileName);
    }

    @RequestMapping("/uploadVideos")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    ResponseResult uploadVideos(@RequestParam("file") MultipartFile file) throws IOException {
        String s = txyunUtils.doUploadVideos(file, "/videos/");
        return ResponseResult.okResult(s);
    }
}
