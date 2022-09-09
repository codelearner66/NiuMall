package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * niumall商城-商品(Goods)表实体类
 *
 * @author ccx
 * @since 2022-07-25 10:06:01
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods")
public class Goods{
    @TableId
    private String id;

    //品牌id
    private Integer brandId;
    //商品类别
    private Integer category;
    //标题
    private String title;
    //简述
    private String simpleDesc;
    //型号
    private String model;
    //库存（所有规格库存总和）
    private Integer inventory;
    //库存单位
    private String inventoryUnit;
    //封面图片
    private String images;
    //原价（单值:10, 区间:10.00-20.00）一般用于展示使用
    private String originalPrice;
    //最低原价
    private String minOriginalPrice;
    //最大原价
    private String maxOriginalPrice;
    //销售价格（单值:10, 区间:10.00-20.00）一般用于展示使用
    private String price;
    //最低价格
    private String minPrice;
    //最高价格
    private String maxPrice;
    //最低起购数量 （默认1）
    private Integer buyMinNumber;
    //最大购买数量（最大数值 100000000, 小于等于0或空则不限）
    private Integer buyMaxNumber;
    //是否扣减库存（0否, 1是）
    private Integer isDeductionInventory;
    //是否上架（下架后用户不可见, 0否, 1是）
    private Integer isShelves;
    //是否首页推荐（0否, 1是）
    private Integer isHomeRecommended;
    //电脑端详情内容
    private String contentWeb;
    //相册图片数量
    private Integer photoCount;
    //销量
    private Integer salesCount;
    //访问次数
    private Integer accessCount;
    //短视频
    private String video;
    //首页推荐图片
    private String homeRecommendedImages;
    //是否存在多个规格（0否, 1是）
    private Integer isExistManySpec;
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
