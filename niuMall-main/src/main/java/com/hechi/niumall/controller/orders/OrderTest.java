package com.hechi.niumall.controller.orders;

import com.hechi.niumall.service.impl.RabbitMqProdcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderTest {
    @Autowired
    RabbitMqProdcer rabbitMqProdcer;

//    @RequestMapping("rabbitTest")
//    public ResponseResult test(){
//        rabbitMqProdcer.producerMessage();
//        return ResponseResult.okResult();
//    }
}
