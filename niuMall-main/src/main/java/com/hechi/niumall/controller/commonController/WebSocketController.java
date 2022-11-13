package com.hechi.niumall.controller.commonController;

import com.hechi.niumall.utils.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: WebSocketController
 * @Description: websocket测试接口
 * @author Alan
 * @date 2022年6月21日
 * @version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    @GetMapping("test")
    public void test() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        	String date = dateFormat.format(new Date());
            WebSocketServer.sendInfo("后端服务推送信息:"+date,"5");
        } catch (Exception e) {
            log.error("分页查询数据接口列表失败", e);
        }
    }
    
}
