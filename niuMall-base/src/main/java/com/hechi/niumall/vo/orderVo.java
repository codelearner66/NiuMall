package com.hechi.niumall.vo;

import com.hechi.niumall.entity.UserAddr;
import lombok.Data;

@Data
public class orderVo {
 private    Long goodsId;
 private    Integer sum;
 private    Integer num;
 private    Integer paymentType;
 private    UserAddr   userAddr;
}
