package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.CommentReplies;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.mapper.CommentRepliesMapper;
import com.hechi.niumall.service.CommentRepliesService;
import com.hechi.niumall.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 评论回复(CommentReplies)表服务实现类
 *
 * @author ccx
 * @since 2023-02-22 01:09:08
 */
@Service("commentRepliesService")
public class CommentRepliesServiceImpl extends ServiceImpl<CommentRepliesMapper, CommentReplies> implements CommentRepliesService {
    @Override
    public long getRepliesNumberById(Long id) {
        LambdaQueryWrapper<CommentReplies> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommentReplies::getCommentId, id);
        return count(queryWrapper);
    }

    @Override
    public Page<CommentReplies> getRepliesServiceById(Long id, int pages) {
        Page<CommentReplies> page1 = new Page<>(pages, 10);
        LambdaQueryWrapper<CommentReplies> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommentReplies::getCommentId, id).eq(CommentReplies::getIsView, 0);
        page(page1, queryWrapper);
        return page1;
    }

    @Override
    public boolean addReplies(CommentReplies comment) {
        final SysUser user = SecurityUtils.getLoginUser().getUser();
        comment.setUserId(user.getId());
        comment.setUserName(user.getNickName());
        comment.setUserAvatar(user.getAvatar());
        comment.setCreateTime(new Date());
        comment.setIsView(0);
        return save(comment);
    }

    @Override
    public boolean deleteReplies(CommentReplies comment) {
        return removeById(comment);
    }
}
