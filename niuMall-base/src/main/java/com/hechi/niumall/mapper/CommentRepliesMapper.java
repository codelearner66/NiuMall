package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.CommentReplies;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 评论回复(CommentReplies)表数据库访问层
 *
 * @author ccx
 * @since 2023-02-22 01:09:08
 */
@Mapper
@Repository
public interface CommentRepliesMapper extends BaseMapper<CommentReplies> {

}

