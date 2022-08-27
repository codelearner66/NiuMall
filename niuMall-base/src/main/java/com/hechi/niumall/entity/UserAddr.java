package com.hechi.niumall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * niumall商城-用户地址(UserAddr)表实体类
 *
 * @author ccx
 * @since 2022-08-06 17:32:21
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_addr")
public class UserAddr  {
    @TableId
    private String id;

    //地址ID
    private String addrId;
    //用户ID
    private String userId;
    //收货人全名
    private String name;
    //固定电话
    private String phone;
    //移动电话
    private String mobile;
    //省份
    private String state;
    //城市
    private String city;
    //区/县
    private String district;
    //收货地址，如：xx路xx号
    private String address;
    //邮政编码,如：110000
    private String receiverZip;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}
