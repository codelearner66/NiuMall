package com.hechi.niumall.controller.commonController;


import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.SysUserService;
import com.hechi.niumall.utils.RedisCache;
import com.hechi.niumall.utils.WebSocketServer;
import com.hechi.niumall.vo.todoListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/common")
public class commonUtilsController {
    @Autowired
    SysUserService userService;
    @Autowired
    RedisCache redisCache;

    @GetMapping("/getLoginIp")
    public ResponseResult getLoginIp(HttpServletRequest request) {
        String ip = null;
        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        // System.out.println("====ipAddresses:" + ipAddresses);
        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            //打印所有头信息
//            String s = headerNames.nextElement();
//            String header = request.getHeader(s);
//            System.out.println(s + "::::" + header);
//        }
//        System.out.println("headerNames:" + JSON.toJSONString(headerNames));
//        System.out.println("RemoteHost:" + request.getRemoteHost());
//        System.out.println("RemoteAddr:" + request.getRemoteAddr());

        String unknown = "unknown";
        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ResponseResult.okResult(ip);
    }

    /**
     * 获取当前在线人数
     *
     * @return
     */
    @GetMapping("/currentUserCount")
    public ResponseResult getCurrentUserCount() {
        return ResponseResult.okResult(WebSocketServer.getOnlineCount());
    }

    @GetMapping("/userCount")
    public ResponseResult getUserCount() {
        Long count = userService.getUserCount();
        return ResponseResult.okResult(count);
    }

    //获取todolist
    @GetMapping("/getTodoList")
    public ResponseResult getTodoList() {
        List<todoListVo> todolist = redisCache.getCacheList("TODOLIST");
        log.info("加载todolist数据：{}", todolist);
        List<todoListVo> result = null;
        if (Objects.nonNull(todolist) && !todolist.isEmpty()) {
            result = todolist.stream().sorted((item1, item2) -> {
                Date date = item1.getDate();
                Date date1 = item2.getDate();
                return date == date1 ? 0 : (date.before(date1) ? -1 : 1);
            }).collect(Collectors.toList());
        }
        return ResponseResult.okResult(result);
    }

    @GetMapping("/deletetoDoList/{id}")
    public ResponseResult deletetoDoList(@PathVariable("id") Long id) {
        List<todoListVo> todolist = redisCache.getCacheList("TODOLIST");
        log.info("加载todolist数据：{}", todolist);
        if (Objects.nonNull(todolist) && !todolist.isEmpty()) {
            todolist.removeIf(item -> item.getId().equals(id));
            redisCache.deleteObject("TODOLIST");
            redisCache.setCacheList("TODOLIST", todolist);
        }
        return ResponseResult.okResult();
    }

    @PostMapping("/setTodoList")
    public ResponseResult setTodoList(@RequestBody todoListVo todoitem) {
        List<todoListVo> todolist = redisCache.getCacheList("TODOLIST");
        log.info("加载todolist数据：{}", todolist);
        if (Objects.isNull(todolist) || todolist.size() == 0) {
            todolist = new ArrayList<>();
            todolist.add(todoitem);
        } else {
            AtomicBoolean flag = new AtomicBoolean(false);
            log.info("todolist {}", todolist);
            for (todoListVo item : todolist) {
                if (item.getId().equals(todoitem.getId())) {
                    item.setTitle(todoitem.getTitle());
                    item.setStatus(todoitem.getStatus());
                    item.setDate(new Date());
                    flag.set(true);
                    break;
                }
            }
            log.info("todolist {}", todolist);
            if (!flag.get()) {
                todoitem.setDate(new Date());
                todolist.add(todoitem);
            }
        }
        redisCache.deleteObject("TODOLIST");
        redisCache.setCacheList("TODOLIST", todolist);
        return ResponseResult.okResult();
    }
}
