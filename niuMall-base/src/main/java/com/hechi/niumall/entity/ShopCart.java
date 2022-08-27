package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * niuMall商城-购物车(ShopCart)表实体类
 *
 * @author ccx
 * @since 2022-08-23 23:31:20
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("shop_cart")
public class ShopCart  {
    @TableId
    private String id;

    //用户id
    private Integer userId;
    //商品id
    private Integer goodsId;
    //标题
    private String title;
    //封面图片
    private String images;
    //总价
    private String sum;
    //销售价格
    private String price;
    //购买数量
    private Integer stock;
    //规格
    private String spec;
    //更新时间
    private Date updateTime;



}
