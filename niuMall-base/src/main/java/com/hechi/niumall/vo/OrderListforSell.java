package com.hechi.niumall.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hechi.niumall.entity.Goods;
import lombok.Data;

@Data
public class OrderListforSell {
    private Long goodsId;
    //订单详情，商品信息
    private String orderContent;
    //商品信息
    @TableField(exist = false)
    private Goods goods;

    private Integer sum;
}
