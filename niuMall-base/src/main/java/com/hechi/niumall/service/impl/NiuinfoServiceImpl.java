package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.Niuinfo;
import com.hechi.niumall.mapper.NiuinfoMapper;
import com.hechi.niumall.service.NiuinfoService;
import com.hechi.niumall.service.SysUserService;
import com.hechi.niumall.vo.NiuinfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Niuinfo)表服务实现类
 *
 * @author ccx
 * @since 2022-11-03 17:20:33
 */
@Slf4j
@Service("niuinfoService")
public class NiuinfoServiceImpl extends ServiceImpl<NiuinfoMapper, Niuinfo> implements NiuinfoService {

    @Autowired
    NiuinfoMapper mapper;
    @Autowired
    SysUserService userService;
    @Override
    public Long getAllUnreadInfo(Long userId) {
        log.info("查询用户未读消息总数，用户id: {}", userId);
        LambdaQueryWrapper<Niuinfo> wrap = new LambdaQueryWrapper<>();
        wrap.eq(Niuinfo::getToid, userId)
                .eq(Niuinfo::getFlag, 0);
        return count(wrap);
    }

    @Override
    public List<NiuinfoVo> getAllUnreadInfoList(Long userId) {
        log.info("查询各个用户未读消息，用户id: {}", userId);
        List<NiuinfoVo> allUnreadInfoList = mapper.getAllUnreadInfoList(userId);
        if (allUnreadInfoList != null) {
            for(NiuinfoVo vo : allUnreadInfoList){
                vo.setUrl(userService.getById(vo.getFromid()).getAvatar());
            }
        }
        return allUnreadInfoList;
    }

    @Override
    public List<Niuinfo> getAllUnreadInfoDetails(Long fromId, Long toId) {
        log.info("查询用户{}-->{}的未读消息", fromId, toId);
        LambdaQueryWrapper<Niuinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Niuinfo::getFromid, fromId)
                .eq(Niuinfo::getToid, toId)
                .eq(Niuinfo::getFlag, 0)
                .orderByAsc(Niuinfo::getSendtime);
        List<Niuinfo> list = list(wrapper);
        log.info("用户{}-->{}的未读消息{}", fromId, toId, list);
        return list;
    }

    @Override
    public List<Niuinfo> getAllreadInfoDetails(Long fromId, Long toId, int page) {
//        查询用户聊天记录
        log.info("用户{},{} 最近聊天记录{}",fromId,toId,page);
        LambdaQueryWrapper<Niuinfo> wrapper = new LambdaQueryWrapper<>();
        List<Long> params = new ArrayList<>(Arrays.asList(fromId, toId));
        wrapper.in(Niuinfo::getFromid, params)
                .in(Niuinfo::getToid, params)
                .eq(Niuinfo::getToid, toId)
                .eq(Niuinfo::getFlag, 1)
                .eq(Niuinfo ::getDeleted,0)
                .orderByAsc(Niuinfo::getSendtime);
        Page<Niuinfo> pageInfo = new Page<>(page, 30);
        page(pageInfo, wrapper);
        List<Niuinfo> records = pageInfo.getRecords();
        List<Niuinfo> collect = records.stream()
                .distinct()
                .sorted((infor1, infor2) -> {
                    Date sendtime = infor1.getSendtime();
                    Date sendtime1 = infor2.getSendtime();
                    return sendtime==sendtime1 ? 0: (sendtime.before(sendtime1)?-1: 1);
                })
                .collect(Collectors.toList());
        pageInfo.setRecords(collect);
        log.info("用户{},{} 最近聊天记录{}",fromId,toId,pageInfo);
       //todo 分页时 可直接返回page对象
        return collect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addNewInfo(Niuinfo niuinfo) {
        return save(niuinfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updataInfoDetails(Niuinfo... niuinfos) {
        log.info("更新用户消息{}", (Object) niuinfos);
        boolean b = false;
        if (niuinfos != null && niuinfos.length > 0) {
            b = updateBatchById(Arrays.asList(niuinfos));
        }
        log.info("更新用户消息{}", b);
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInfo(Long... ids) {
        log.info("删除用户消息{}", (Object) ids);
        boolean b = false;
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                b = removeById(id);
                if (!b) {
                    throw new RuntimeException("删除数据出错！");
                }
            }
        }
        log.info("删除用户消息{}", b);
        return b;
    }
}
