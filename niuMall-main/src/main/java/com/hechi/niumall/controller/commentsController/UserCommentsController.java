package com.hechi.niumall.controller.commentsController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hechi.niumall.entity.GoodsComment;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.GoodsCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class UserCommentsController {
    @Autowired
    GoodsCommentService goodsCommentService;

    /**
     * 添加评论
     *
     * @param goodsComment
     * @return
     */
    @PostMapping("/addGoodsComment")
    public ResponseResult addGoodsComment(@RequestBody GoodsComment goodsComment) {
        final GoodsComment goodsComment1 = goodsCommentService.addGoodsComment(goodsComment);
        return ResponseResult.okResult(goodsComment1);
    }

    @RequestMapping("/isCommented")
    public ResponseResult isCommented(@RequestParam("goodsId") long goodsId) {
      return ResponseResult.okResult(goodsCommentService.isCommented(goodsId));
    }
    @RequestMapping("/canComment")
    public ResponseResult canlComment(@RequestParam("goodsId") long goodsId) {
        return ResponseResult.okResult(goodsCommentService.canComment(goodsId));
    }
    @PostMapping("/deleteGoodsComment")
    public ResponseResult deleteGoodsComment(@RequestBody GoodsComment goodsComment) {
        final boolean b = goodsCommentService.deleteGoodsComment(goodsComment);
        return b ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @PostMapping("/modifyGoodsComment")
    public ResponseResult modifyGoodsComment(@RequestBody GoodsComment goodsComment) {
        final GoodsComment goodsComment1 = goodsCommentService.modifyGoodsComment(goodsComment);
        return ResponseResult.okResult(goodsComment1);
    }

    @GetMapping("/getGoodsCommentsById")
    ResponseResult getGoodsCommentsById(@RequestParam("id") int id, @RequestParam("pages") int pages) {
        final Page<GoodsComment> goodsCommentsById = goodsCommentService.getGoodsCommentsById(id, pages);
        return ResponseResult.okResult(goodsCommentsById);
    }

    @PostMapping("/uploadeCommentsImages")
    public ResponseResult uploadeCommentsImages(@RequestParam("file") MultipartFile[] file) throws IOException {
        List<String> list = goodsCommentService.uploadeCommentsImages(file);
        return ResponseResult.okResult(list);
    }
}
