package com.hechi.niumall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private  Integer id;
    //商品id
    private String gId;
    //图片地址
    private String  imageUrl;
}
