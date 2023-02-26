package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * 评论回复(CommentReplies)表实体类
 *
 * @author ccx
 * @since 2023-02-22 01:09:08
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment_replies")
public class CommentReplies  {
    @TableId
    private String id;

    //评论根对象id 指定评论的商品
    private Long rootId;
    //评论对象id 指定评论的评论
    private Long commentId;
    //用户id
    private Long userId;
    //用户名称
    private String userName;
    //用户头像
    private String userAvatar;
    //被评论对方id
    private Long yoursId;
    //被评论方名称
    private String yourName;
    //评价内容
    private String repliesDesc;
    //是否可见
    private Integer isView;
    //评价时间
    private Date createTime;
    //删除标识 
    private Integer delFlag;



}
