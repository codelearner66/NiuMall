package com.hechi.niumall.service.impl;

import com.alibaba.fastjson.JSON;
import com.hechi.niumall.config.RabbitMQConfig;
import com.hechi.niumall.entity.Order;
import com.hechi.niumall.service.AlipayService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class RabbitMqConsumer {

    @Autowired
    AlipayService alipayServiceImpl;


    //    死信队列消费者
    @RabbitListener(queues = { RabbitMQConfig.DEADEED_QUEUE })
    @RabbitHandler
    public void deadQueue(Message msg, Channel channel) throws IOException, InterruptedException {
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();
        log.error("接收到的消息体{}",msg.getBody());
        //1.解析订单对象
        Order order=  JSON.parseObject(msg.getBody(),Order.class);
        //2. 调用阿里 查单接口查询订单
        if (order != null&&alipayServiceImpl.checkOrderStatus(order.getOrderId())) {
            //   消费正常
            channel.basicAck(deliveryTag, false);
            //todo 当支付宝用户未扫码或者登录时 调用查单接口会报错
        }
        else {
            //消费异常重回队列
            channel.basicNack(deliveryTag, false,true);
        }
    }
}
