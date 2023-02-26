package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.GoodsComment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * 商品评价(GoodsComment)表服务接口
 *
 * @author ccx
 * @since 2023-02-21 15:25:22
 */
public interface GoodsCommentService extends IService<GoodsComment> {
    /**
     * 添加一个评论
     *
     * @param comment 新的评论
     * @return 评论对象
     */
    GoodsComment addGoodsComment(GoodsComment comment);

    /**
     * 删除一个评论
     *
     * @param comment 需要删除的评论对象
     * @return true / false
     */
    boolean deleteGoodsComment(GoodsComment comment);

    /**
     * 修改评论
     *
     * @param comment 新的评论对象
     * @return 修改以后的对象
     */
    GoodsComment modifyGoodsComment(GoodsComment comment);

    /**
     * 通过商品id查询评论
     * @param id 商品id
     * @param pages 页码
     * @return 评论列表
     */
    Page<GoodsComment> getGoodsCommentsById(long id, int pages);

    Page<GoodsComment> getAllComments(int pages);

    boolean isCommented(long goodsId);

    /**
     * 是否有资格评论
     * @param goodsId
     * @return
     */
    boolean canComment(long goodsId);

    List<String> uploadeCommentsImages(MultipartFile[] file) throws IOException;
}

