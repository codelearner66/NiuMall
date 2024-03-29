package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.*;
import com.hechi.niumall.enums.AppHttpCodeEnum;
import com.hechi.niumall.mapper.SysUserMapper;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.*;
import com.hechi.niumall.utils.*;
import com.hechi.niumall.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 用户表(SysUser)表服务实现类
 *
 * @author ccx
 * @since 2022-07-09 16:29:13
 */
@Slf4j
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    RedisCache redisCache;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysRoleMenuService sysRoleMenuService;
    @Autowired
    SysMenuService sysMenuService;
    @Autowired
    MessageUtils messageUtils;
    @Autowired
    MailUtils mailUtils;
    @Autowired
    TxyunUtils txyunUtils;

    private static final Lock lock=new ReentrantLock();
    @Override
    public ResponseResult getUser() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public LoginUser getUserByPhoneNumber(String phoneNumber) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getPhonenumber, phoneNumber).or().eq(SysUser::getEmail, phoneNumber);
        SysUser user = getOne(lambdaQueryWrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //查询权限信息封装
        List<SysUserRole> sysUserRolesByUserId = sysUserRoleService.getSysUserRolesByUserId(user.getId());
        List<Long> collect = sysUserRolesByUserId.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        collect.forEach(System.out::println);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.getSysRoleMenuList(collect);
        List<Long> collect1 = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
        List<SysMenu> sysMenuList = sysMenuService.getSysMenuList(collect1);
        List<String> collect2 = sysMenuList.stream().map(SysMenu::getPerms).collect(Collectors.toList());
        //返回用户信息
        //查询权限信息封装
        return new LoginUser(user, collect2);
    }

    @Override
    public ResponseResult isExist(SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserName, user.getUserName() != null ? user.getUserName() : " ")
                .or().eq(SysUser::getEmail, user.getEmail() != null ? user.getEmail() : "-1")
                .or().eq(SysUser::getPhonenumber, user.getPhonenumber() != null ? user.getPhonenumber() : "-1");
        SysUser one = getOne(wrapper);
        return one != null ? ResponseResult.okResult(200, "帐号存在") : ResponseResult.errorResult(200, "帐号不存在");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult addUser(SysUser user) {
        if ("帐号存在".equals(isExist(user).getMsg())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNickName("开心的萝卜头");
        user.setSex("0");
        user.setAvatar("https://miumall-1306251195.cos.ap-chengdu.myqcloud.com/headers/header.jpg");
        user.setCreateTime(new Date());
        boolean save = save(user);
        boolean save1 = false;
        if (save) {
            // 给初始帐号赋给限权
            save1 = sysUserRoleService.save(new SysUserRole(user.getId(), 4L));
        }

        return save && save1 ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
    }

    @Override
    @Transactional
    public ResponseResult findpassword(SysUser user) throws Exception {
        //判断是邮箱还是手机号 选择相应的工具类发送消息
        Random random = new Random();
        int code = random.nextInt(100000);
        SysUser one = null;
        if (user.getPhonenumber() != null && user.getEmail() == null) {
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser::getPhonenumber, user.getPhonenumber());
            one = getOne(queryWrapper);
            messageUtils.sentMessage(user.getPhonenumber(), Integer.toString(code));
        }
        if (user.getEmail() != null && user.getPhonenumber() == null) {
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser::getEmail, user.getEmail());
            one = getOne(queryWrapper);
            mailUtils.sentMailCode(user.getEmail(), "重置密码", "mallNewPassword", Integer.toString(code));
        }
        assert one != null;
        one.setPassword(passwordEncoder.encode(Integer.toString(code)));
        return updateById(one) ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult checkPassword(String password) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.getPassword();
        boolean matches = passwordEncoder.matches(password, loginUser.getPassword());
        return matches ? ResponseResult.okResult() : ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updataUser(SysUser user) {
        log.info("删除redis数据{}","NMLogin:"+user.getId());
        redisCache.deleteObject("NMLogin:"+user.getId());
        boolean b=false;
        if(lock.tryLock()){
            try {
                log.info("更新数据库");
                b = updateById(user);
                SysUser byId = getById(user.getId());
                LoginUser userDetails = (LoginUser)userDetailsService.loadUserByUsername(byId.getUserName());
                log.info("更新缓存");
                redisCache.setCacheObject("NMLogin:"+user.getId(),userDetails);
            }finally {
                lock.unlock();
            }
        }
        return  b;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String uploadHeader(MultipartFile file) throws IOException {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String avatar = user.getAvatar();
        String pre = "https://miumall-1306251195.cos.ap-chengdu.myqcloud.com/";
        if (!avatar.endsWith("header.jpg")) {
            txyunUtils.doDelete(avatar.replace(pre, ""));
        }
        String s = txyunUtils.doUpdata(file, "/headers/");
        user.setAvatar(s);
        this.updataUser(user);
        return s;
    }

    @Override
    public List<SysUser> getUserByKey(String key) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SysUser ::getNickName,key)
                .or()
                .like(SysUser ::getUserName,key)
                .last("limit 8");
        return list(queryWrapper);
    }

    @Override
    public Long getUserCount() {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser ::getType,0);
        long count = count(queryWrapper);
        return count;
    }
}
