package com.hechi.niumall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hechi.niumall.entity.Niuinfo;
import com.hechi.niumall.vo.NiuinfoVo;

import java.util.List;


/**
 * (Niuinfo)表服务接口
 *
 * @author ccx
 * @since 2022-11-03 17:20:33
 */
public interface NiuinfoService extends IService<Niuinfo> {

    /**
     * 获取用户所有未读消息数量
     * @param userId 用户id
     * @return  未读消息总数
     */
    public Long getAllUnreadInfo(Long userId);

    /**
     * 获取未读信息用户列表
     * @param userId 用户id
     * @return 未读用户信息列表
     */
    public List<NiuinfoVo> getAllUnreadInfoList(Long userId);
    /**
     * 获取用户未读消息详情
     * @param fromId 发送者id
     * @param toId  接收者id
     * @return   消息列表
     */
      public List<Niuinfo> getAllUnreadInfoDetails(Long fromId,Long toId);

    /**
     * 分页获取用户已读消息
     * @param fromId 发送者id
     * @param toId   接收者id
     * @param page  分页数据
     * @return     消息列表
     */
      public List<Niuinfo> getAllreadInfoDetails(Long fromId, Long toId,int page);

    /**
     * 添加一条新消息
     * @param niuinfo 新消息
     * @return true / false
     */
    public boolean addNewInfo(Niuinfo niuinfo);
    /**
     * 更新用户消息
     * @param niuinfos 需要更新的消息
     * @return trur / false
     */
   public boolean updataInfoDetails(Niuinfo... niuinfos);

    /**
     * 删除用户消息
     * @param ids 消息id
     * @return true/false
     */
   public  boolean deleteInfo(Long ... ids);
}

