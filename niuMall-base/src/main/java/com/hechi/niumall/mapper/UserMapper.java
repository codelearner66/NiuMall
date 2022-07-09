package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * B2C商城-后台用户表(User)表数据库访问层
 *
 * @author ccx
 * @since 2022-07-09 14:20:11
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}

