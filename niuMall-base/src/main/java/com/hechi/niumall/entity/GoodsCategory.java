package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * niumall商城-商品分类(GoodsCategory)表实体类
 *
 * @author ccx
 * @since 2022-07-14 21:48:55
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods_category")
public class GoodsCategory  {
    @TableId
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
    //创建人的用户id
    private Long createBy;
    //创建时间
    private Date createTime;
    //更新人
    private Long updateBy;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}
