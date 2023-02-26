package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商品评价(GoodsComment)表实体类
 *
 * @author ccx
 * @since 2023-02-21 15:25:22
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods_comment")
public class GoodsComment {
    @TableId
    private String id;

    //商品编号
    private Long goodsId;
    //商品名称
    private String goodsName;
    //是否有图片上传
    private Integer isImages;
    //上传图片
    private String images;
    //购买Id
    private String buyerId;
    private String buyerAvatar;
    //购买人
    private String buyerName;
    //评价内容
    private String goodsDesc;
    @TableField(exist = false)
    private Integer childNum;
    //评价等级，0表示非常差，1表示差，2表示一般，3表示好，4表示非常好
    private Integer goodsRank;
    //是否置顶 0 是 1 否
    private Integer isTop;
    //评价时间
    private Date createTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;

}
