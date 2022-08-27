package com.hechi.niumall.vo;

import com.hechi.niumall.entity.ShopCart;
import com.hechi.niumall.entity.UserAddr;
import lombok.Data;

import java.util.List;

@Data
public class shopCartVo {
  private List<ShopCart> list;
    private UserAddr userAddr;
}
