package com.hechi.niumall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hechi.niumall.entity.Niuinfo;
import com.hechi.niumall.vo.NiuinfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Niuinfo)表数据库访问层
 *
 * @author ccx
 * @since 2022-11-03 17:20:33
 */
@Mapper
@Repository
public interface NiuinfoMapper extends BaseMapper<Niuinfo> {
    /**
     * 获取未读信息用户列表
     * @param userId 用户id
     * @return 未读用户信息列表
     */
    public List<NiuinfoVo> getAllUnreadInfoList(@Param("userId") Long userId);
}

