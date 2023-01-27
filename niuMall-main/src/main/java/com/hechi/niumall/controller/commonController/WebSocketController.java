package com.hechi.niumall.controller.commonController;

import com.hechi.niumall.entity.Niuinfo;
import com.hechi.niumall.result.ResponseResult;
import com.hechi.niumall.service.NiuinfoService;
import com.hechi.niumall.utils.SecurityUtils;
import com.hechi.niumall.vo.NiuinfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ccx
 * @date 2022/11/13 21:43
 */
@Slf4j
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    @Autowired
    NiuinfoService niuinfoService;

    @GetMapping("test")
    public void test() {
        Niuinfo niuinfo=new Niuinfo();
        niuinfo.setContent("''65utyu");
        Niuinfo info=  niuinfoService.getNiuinfoByContent(niuinfo);
        log.info("查询的结果：{}",info);
    }

    //获取用户未读总数
    @GetMapping("/getAllUnreadCount")
    public ResponseResult getAllUnreadCount() {
        Long userId = SecurityUtils.getUserId();
        Long allUnreadInfo = niuinfoService.getAllUnreadInfo(userId);
        return ResponseResult.okResult(allUnreadInfo);
    }

    //    未读信息用户列表
    @GetMapping("/getNotReadUserList")
    public ResponseResult getNotReadUserList() {
        Long userId = SecurityUtils.getUserId();
        List<NiuinfoVo> allUnreadInfoList = niuinfoService.getAllUnreadInfoList(userId);
        return ResponseResult.okResult(allUnreadInfoList);
    }
//todo 排除自己 名称和消息对照
    //分页获取用户历史会话列表
    @GetMapping("/getAllUserList/{pages}")
    public ResponseResult getAllUserList(@PathVariable("pages") Long pages) {
        Long userId = SecurityUtils.getUserId();
        Map<String, Object> list = niuinfoService.getAllUserList(userId, pages);
        return ResponseResult.okResult(list);
    }

    //通过用户id 或用户名查询用户
    @GetMapping("/getUserListByKey/{key}")
    public ResponseResult getUserListByKey(@NotNull @PathVariable("key") String key) {
        Long userId = SecurityUtils.getUserId();
        List<NiuinfoVo> list = niuinfoService.getUserListByKey(key, userId);
        return ResponseResult.okResult(list);
    }

    // 获取用户未读消息详情
    @RequestMapping("/getAllUnreadInfoDetails")
    public ResponseResult getAllUnreadInfoDetails(Long fromId, Long toId) {
        Long userId = SecurityUtils.getUserId();
        if (SecurityUtils.isAdmin()) {
            userId = toId;
        }
        List<Niuinfo> allUnreadInfoDetails = niuinfoService.getAllUnreadInfoDetails(fromId, userId);
        return ResponseResult.okResult(allUnreadInfoDetails);
    }

    //分页获取用户已读消息
    @RequestMapping("/getAllreadInfoDetails")
    public ResponseResult getAllreadInfoDetails(Long fromId, Long toId, int page) {
        Long userId = SecurityUtils.getUserId();
        List<Niuinfo> allreadInfoDetails = niuinfoService.getAllreadInfoDetails(fromId, userId, page);
        return ResponseResult.okResult(allreadInfoDetails);
    }

    /**
     * 添加一条新消息
     *
     * @param niuinfo 新消息
     * @return true / false
     */
    @PostMapping("/addNewInfo")
    public ResponseResult addNewInfo(@RequestBody Niuinfo niuinfo) {

        //todo 用户信息进来以后先入库 后调用 websocket 接口回执到指定用户
        boolean b = niuinfoService.addNewInfo(niuinfo);
        return ResponseResult.okResult();
    }

    /**
     * 更新用户消息
     *
     * @param niuinfos 需要更新的消息
     * @return trur / false
     */
    public ResponseResult updataInfoDetails(Niuinfo... niuinfos) {
        niuinfoService.updataInfoDetails(niuinfos);
        return ResponseResult.okResult();
    }

    //更新信息为已读
    @PostMapping("/readedInfo")
    public ResponseResult readedInfo(@RequestBody Niuinfo niuinfo) {
        niuinfo.setSendtime(new Date());
        log.info("更新信息接收到的数据：{}", niuinfo);
        boolean b = niuinfoService.readedInfo(niuinfo);
        return ResponseResult.okResult(b);
    }

    /**
     * 删除用户消息
     *
     * @param ids 消息id
     * @return true/false
     */
    public ResponseResult deleteInfo(Long... ids) {
        boolean b = niuinfoService.deleteInfo(ids);
        return ResponseResult.okResult();
    }
}
