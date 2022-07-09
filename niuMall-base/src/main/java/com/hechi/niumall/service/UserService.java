package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.User;


/**
 * B2C商城-后台用户表(User)表服务接口
 *
 * @author ccx
 * @since 2022-07-09 14:20:11
 */
public interface UserService extends IService<User> {
  public User getUser();
}

