package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.CommentReplies;


/**
 * 评论回复(CommentReplies)表服务接口
 *
 * @author ccx
 * @since 2023-02-22 01:09:08
 */
public interface CommentRepliesService extends IService<CommentReplies> {
    //    通过评论id查询回复数量
    long getRepliesNumberById(Long id);

    //     根据评论id 查询回复
    Page<CommentReplies> getRepliesServiceById(Long id, int pages);

    boolean addReplies(CommentReplies comment);

    boolean deleteReplies(CommentReplies comment);
}

