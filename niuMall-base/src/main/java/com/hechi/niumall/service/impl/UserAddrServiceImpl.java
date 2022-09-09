package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.LoginUser;
import com.hechi.niumall.entity.SysUser;
import com.hechi.niumall.entity.UserAddr;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.mapper.UserAddrMapper;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.UserAddrService;
import com.hechi.niumall.utils.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * niumall商城-用户地址(UserAddr)表服务实现类
 *
 * @author ccx
 * @since 2022-08-06 17:32:21
 */
@Service("userAddrService")
public class UserAddrServiceImpl extends ServiceImpl<UserAddrMapper, UserAddr> implements UserAddrService {
    //    获取当前用户 用户地址
    @Override
    public ResponseResult getAddrByUser() {
        return getData(SecurityUtils.getUserId());
    }

    //通过userid获取用户地址
    @Override
    public ResponseResult getAddrByUserId(Long id) {
        return getData(id);
    }

    @Cacheable(value = "userAddr", key = "'userId:'+#id")
    public ResponseResult getData(Long id) {
        LambdaQueryWrapper<UserAddr> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddr::getUserId, id);
        List<UserAddr> list = list(queryWrapper);
        return ResponseResult.okResult(list);
    }

    @Override
    @Cacheable(value = "userAddr", key = "'userAddr:'+#id")
    public ResponseResult getAddrById(Long id) {
        UserAddr byId = getById(id);
        if (byId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_ADDR_NOTEXIST);
        }
        return ResponseResult.okResult(byId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createAddr(UserAddr addr) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        addr.setUserId(String.valueOf(user.getId()));
        if (addr.getName() == null) {
            addr.setName(user.getNickName());
        }
        if (addr.getMobile() == null) {
            addr.setMobile(user.getPhonenumber());
        }
        addr.setCreateTime(new Date());
        boolean save = save(addr);
        return save
                ? ResponseResult.okResult(addr.getId())
                : ResponseResult.errorResult(AppHttpCodeEnum.USER_ADD_ADDR_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "userAddr", key = "'userAddr:'+#addr.id")
    public ResponseResult updateAddr(UserAddr addr) {
        UserAddr byId = getById(addr.getId());
        if (byId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_ADDR_NOTEXIST);
        }
        boolean b = updateById(addr);
        return b
                ? ResponseResult.okResult()
                : ResponseResult.errorResult(AppHttpCodeEnum.USER_UPDATA_ADDR_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "userAddr", key = "'userAddr:'+#id")
    public ResponseResult deleteAddrById(Long id) {
        LambdaQueryWrapper<UserAddr> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddr::getId, id).eq(UserAddr::getUserId, SecurityUtils.getUserId());
        int delete = baseMapper.delete(queryWrapper);
        return delete > 0
                ? ResponseResult.okResult()
                : ResponseResult.errorResult(AppHttpCodeEnum.USER_DELETE_ADDR_ERROR);
    }
}
