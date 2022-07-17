package com.hechi.niumall.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * (SysRoleMenu)表实体类
 *
 * @author ccx
 * @since 2022-07-17 21:16:55
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
public class SysRoleMenu  {
    //角色ID
     @TableId
    private Long roleId;
    //菜单id@TableId
    private Long menuId;




}
