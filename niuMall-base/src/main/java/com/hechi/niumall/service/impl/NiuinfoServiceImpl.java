package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.Niuinfo;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.mapper.NiuinfoMapper;
import com.hechi.niumall.service.NiuinfoService;
import com.hechi.niumall.service.SysUserService;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.vo.NiuinfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        List<NiuinfoVo> alllastUser = mapper.getAlllastUser(userId);
        allUnreadInfoList.addAll(alllastUser);
        List<NiuinfoVo> collect = allUnreadInfoList.stream().distinct().peek(niuinfoVo -> {
            niuinfoVo.setUrl(userService.getById(niuinfoVo.getFromid()).getAvatar());
            niuinfoVo.setLastInfo(mapper.getLastInfo(niuinfoVo.getFromid(), userId));
            niuinfoVo.setCount(niuinfoVo.getCount() == null ? 0 : niuinfoVo.getCount());
        }).filter(item-> !item.getFromid().equals(userId)).collect(Collectors.toList());
        for (int i = 0; i < collect.size() - 1; i++) {
            NiuinfoVo temp = collect.get(i);
            for (int j = i + 1; j < collect.size(); j++) {
                NiuinfoVo niuinfoVo = collect.get(j);
                if (temp.getFromid().equals(niuinfoVo.getFromid())) {
                    if (temp.getCount() == 0 && niuinfoVo.getCount() != 0) {
                        collect.remove(temp);
                    } else {
                        collect.remove(niuinfoVo);
                    }
                }
            }
        }
        return collect;
    }

    @Override
    public NiuinfoVo getNiuinfo(Long fromId, Long toId) {
        //查询单个用户的未读消息
        NiuinfoVo unreadInfoById = mapper.getUnreadInfoById(fromId, toId);
        unreadInfoById.setUrl(userService.getById(unreadInfoById.getFromid()).getAvatar());
        //todo获取最后一条消息
        String lastInfo = mapper.getLastInfo(fromId, toId);
        unreadInfoById.setLastInfo(lastInfo);
        return unreadInfoById;
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
        log.info("用户{},{} 最近聊天记录{}", fromId, toId, page);
        LambdaQueryWrapper<Niuinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(nWrapper -> nWrapper.eq(Niuinfo::getFromid, fromId).eq(Niuinfo::getToid, toId)
                .or()
                .eq(Niuinfo::getFromid, toId).eq(Niuinfo::getToid, fromId))
                .and(niuinfoLambdaQueryWrapper -> niuinfoLambdaQueryWrapper.eq(Niuinfo::getDeleted, 0))
                .orderByDesc(Niuinfo::getSendtime);
        Page<Niuinfo> pageInfo = new Page<>(page, 30);
        page(pageInfo, wrapper);
        List<Niuinfo> records = pageInfo.getRecords();
        List<Niuinfo> collect = records.stream()
                .distinct()
                .sorted((infor1, infor2) -> {
                    Date sendtime = infor1.getSendtime();
                    Date sendtime1 = infor2.getSendtime();
                    return sendtime == sendtime1 ? 0 : (sendtime.before(sendtime1) ? -1 : 1);
                })
                .collect(Collectors.toList());
        pageInfo.setRecords(collect);
        log.info("用户{},{} 最近聊天记录{}", fromId, toId, pageInfo);
        //todo 分页时 可直接返回page对象
        return collect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addNewInfo(Niuinfo niuinfo) {
        log.info("插入新消息: {}", niuinfo);
        niuinfo.setType(1);
        niuinfo.setFlag(0);
        niuinfo.setDeleted(0);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean readedInfo(Niuinfo niuinfo) {
        LambdaUpdateWrapper<Niuinfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Niuinfo::getFromid, niuinfo.getFromid())
                .eq(Niuinfo::getToid, niuinfo.getToid())
                .le(Niuinfo::getSendtime, niuinfo.getSendtime())
                .set(Niuinfo::getFlag, 1);
        return update(null, updateWrapper);
    }

    @Override
    public Map<String, Object> getAllUserList(Long userId, Long pages) {
        LambdaQueryWrapper<Niuinfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Niuinfo::getToid, userId).ne(Niuinfo::getFromid, userId).groupBy(Niuinfo::getFromid);
        Page<Niuinfo> page = new Page<>(pages, 8);
        page(page, queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotal());
        map.put("current", page.getCurrent());
        map.put("hasNext", page.hasNext());
        map.put("hasPre", page.hasPrevious());
        map.put("pageCount", page.getPages());
        List<NiuinfoVo> collect = page.getRecords().stream().map(niuinfo -> {
            NiuinfoVo niuinfoVo = new NiuinfoVo();
            niuinfoVo.setFromid(niuinfo.getFromid());
            niuinfoVo.setFromname(niuinfo.getFromname());
            niuinfoVo.setCount(0L);
            niuinfoVo.setLastInfo("");
            niuinfoVo.setUrl(userService.getById(niuinfo.getFromid()).getAvatar());
            return niuinfoVo;
        }).collect(Collectors.toList());
        map.put("data", collect);
        return map;
    }

    @Override
    public List<NiuinfoVo> getUserListByKey(String key, Long userId) {
        log.info("进入用户消息模糊查询");
        List<NiuinfoVo> niuinfoVolist = new ArrayList<>();
        List<SysUser> userList = userService.getUserByKey(key);
        if (userList != null && userList.size() > 0) {
            userList.forEach(user -> {
                if (!user.getId().equals(SecurityUtils.getUserId())) {
                    NiuinfoVo niuinfoVo = new NiuinfoVo();
                    niuinfoVo.setCount(0L);
                    niuinfoVo.setLastInfo("");
                    niuinfoVo.setFromid(user.getId());
                    niuinfoVo.setFromname(user.getNickName());
                    niuinfoVo.setUrl(user.getAvatar());
                    niuinfoVolist.add(niuinfoVo);
                }
            });
        }
        return niuinfoVolist;
    }

    @Override
    public Niuinfo getNiuinfoByContent(Niuinfo niuinfo) {
        Niuinfo inuinfo= mapper.getNiuinfoByContent(niuinfo);
        return inuinfo;
    }
}
