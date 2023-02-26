package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.GoodsComment;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.mapper.GoodsCommentMapper;
import com.hechi.niumall.service.CommentRepliesService;
import com.hechi.niumall.service.GoodsCommentService;
import com.hechi.niumall.service.OrderService;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.utils.TxyunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品评价(GoodsComment)表服务实现类ff
 *
 * @author ccx
 * @since 2023-02-21 15:25:22
 */
@Service("goodsCommentService")
public class GoodsCommentServiceImpl extends ServiceImpl<GoodsCommentMapper, GoodsComment> implements GoodsCommentService {
    @Autowired
    GoodsCommentMapper goodsCommentMapper;
    @Autowired
    CommentRepliesService repliesService;
    @Autowired
    OrderService orderService;
    @Autowired
    TxyunUtils txyunUtils;

    @Override
    @CacheEvict(value = "Comment", key = "#comment.id+'ID'")
    public GoodsComment addGoodsComment(GoodsComment comment) {
        final SysUser user = SecurityUtils.getLoginUser().getUser();
        comment.setBuyerId(user.getId().toString());
        comment.setBuyerAvatar(user.getAvatar());
        comment.setBuyerName(user.getNickName());
        comment.setIsTop(comment.getGoodsRank() > 3 ? 0 : 1);
        comment.setCreateTime(new Date());
        return save(comment) ? comment : null;
    }

    @Override
    @CacheEvict(value = "Comment", key = "#comment.id+'ID'")
    public boolean deleteGoodsComment(GoodsComment comment) {
        return removeById(comment);
    }

    @Override
    @CacheEvict(value = "Comment", key = "#comment.id+'ID'")
    public GoodsComment modifyGoodsComment(GoodsComment comment) {
        final boolean b = updateById(comment);
        GoodsComment byId = new GoodsComment();
        if (b) {
            byId = getById(comment.getId());
        }
        return byId;
    }

    @Override
    @Cacheable(value = "Comment", key = "#id+'P'+#pages")
    public Page<GoodsComment> getGoodsCommentsById(long id, int pages) {
        LambdaQueryWrapper<GoodsComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoodsComment::getGoodsId, id).orderBy(true, true, GoodsComment::getIsTop);
        Page<GoodsComment> commentPage = new Page<>(pages, 10);
        page(commentPage, wrapper);
        List<GoodsComment> records = commentPage.getRecords();
        List<GoodsComment> collect = records.stream().sorted(Comparator.comparing(GoodsComment::getCreateTime)).collect(Collectors.toList());
        collect.forEach(item -> {
            final long l = Long.parseLong(item.getId());
            item.setChildNum(Math.toIntExact(repliesService.getRepliesNumberById(l)));
        });
        commentPage.setRecords(collect);
        return commentPage;
    }

    @Override
    @Cacheable(value = "Comment", key = "'All'+#pages")
    public Page<GoodsComment> getAllComments(int pages) {
        LambdaQueryWrapper<GoodsComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderBy(true, true, GoodsComment::getIsTop);
        Page<GoodsComment> commentPage = new Page<>(pages, 10);
        page(commentPage, wrapper);
        List<GoodsComment> records = commentPage.getRecords();
        //todo 添加子评论字段数量
        List<GoodsComment> collect = records.stream().sorted(Comparator.comparing(GoodsComment::getCreateTime)).collect(Collectors.toList());
        commentPage.setRecords(collect);
        return commentPage;
    }

    //    是否评论过
    @Override
    public boolean isCommented(long goodsId) {
        LambdaQueryWrapper<GoodsComment> wrap = new LambdaQueryWrapper<>();
        wrap.eq(GoodsComment::getGoodsId, goodsId).eq(GoodsComment::getBuyerId, SecurityUtils.getUserId());
        final GoodsComment one = getOne(wrap);
        return Objects.nonNull(one) && one.getId() != null;
    }

    @Override
    public boolean canComment(long goodsId) {
        final Order orderByUserIdandGoodsId = orderService.getOrderByUserIdandGoodsId(SecurityUtils.getUserId(), goodsId);
        return Objects.nonNull(orderByUserIdandGoodsId) && orderByUserIdandGoodsId.getId() != null&&!isCommented(goodsId);
    }

    @Override
    public List<String> uploadeCommentsImages(MultipartFile[] file) throws IOException {
        List<String> list = new ArrayList<>();
        if (file != null && file.length > 0) {
            for (MultipartFile multipartFile : file) {
                list.add(txyunUtils.doUpdata(multipartFile, "/commentsImages/"));
            }
        }
        return list;
    }
}
