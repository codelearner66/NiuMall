package com.hechi.niumall.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * (SysUserRole)表实体类
 *
 * @author ccx
 * @since 2022-07-17 21:16:05
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_role")
public class SysUserRole  {
    //用户id
    @TableId
    private Long userId;
    //角色id@TableId
    private Long roleId;




}
