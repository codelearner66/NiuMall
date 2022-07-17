package com.hechi.niumall.vo;

import lombok.Data;

@Data
public class GoodsCategoryVo {

    private String id;

    //父id
    private Integer pid;
    //icon图标
    private String icon;
    //名称
    private String goodsCategoryName;
    //副标题
    private String viceName;
    //描述
    private String goodsCategoryDescribe;
    //大图片
    private String bigImages;
    //是否首页推荐（0否, 1是）
    private Integer isHomeRecommended;
    //排序
    private Integer sort;
    //是否启用（0否，1是）
    private Integer isEnable;
}
