package com.hechi.niumall.entity;

import lombok.Data;

import java.util.Date;

@Data
public class EditotrNotification {
    //消息id@TableId
    private String id;
    //通知标题
    private String title;
    //通知主题源码
    private String content;
    //    通知前端回显
    private  String html;
    //图片资源
    private String images;

    //通知类型 0管理员通知 1 用户通知
    private Integer type;

    //是否已读 0 未读，1 已读
    private Integer flag;
    //消息是否被删除
    private Integer isDeleted;
    //创建人id
    private Long createBy;
    //创建时间
    private Date createDate;
}
