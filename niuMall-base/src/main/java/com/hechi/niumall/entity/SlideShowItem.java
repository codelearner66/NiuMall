package com.hechi.niumall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ccx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/*
 * 轮播图实例
 */
public class SlideShowItem {
    private Integer id;
    //商品id
    private String gId;
    //    简单描述
    private String desc;
    //图片地址
    private String imageUrl;
    //    是否启用
    private Integer isEnable;
    //    更新时间
    private Date updateTime;
    //    更新人
    private String updatedBy;
    //    创建时间
    private Date createTime;
    //  创建人
    private String createdBy;

}
