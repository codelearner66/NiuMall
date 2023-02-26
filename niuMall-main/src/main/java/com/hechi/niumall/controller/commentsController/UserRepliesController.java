package com.hechi.niumall.controller.commentsController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hechi.niumall.entity.CommentReplies;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.CommentRepliesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRepliesController {
    @Autowired
    CommentRepliesService repliesService;

    @GetMapping("/getRepliesServiceById")
    public ResponseResult getRepliesServiceById(@RequestParam("id") long id, @RequestParam("pages") int pages) {
        final Page<CommentReplies> repliesServiceById = repliesService.getRepliesServiceById(id, pages);
        return ResponseResult.okResult(repliesServiceById);
    }

    @PostMapping("/addReplies")
    public ResponseResult addReplies(@RequestBody CommentReplies postMap) {
        final boolean b = repliesService.addReplies(postMap);
        return ResponseResult.okResult(b);
    }

    @PostMapping("/deleteReplies")
    public ResponseResult deleteReplies(@RequestBody CommentReplies postMap) {
        final boolean b = repliesService.deleteReplies(postMap);
        return ResponseResult.okResult(b);
    }
}
