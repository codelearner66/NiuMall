package com.hechi.niumall.service.impl;

import com.alibaba.fastjson.JSON;
import com.hechi.niumall.config.RabbitMQConfig;
import com.hechi.niumall.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author ccx
 * @date 2022 10:23 21:04
 */
@Slf4j
@Service
public class RabbitMqProdcer {
    @Autowired
    RabbitTemplate rabbitTemplate;
    private static  final ConcurrentSkipListMap<String,Object> MESSAGE_LIST = new ConcurrentSkipListMap<>();

//    public void producerMessage() {
//        String message = "test 001 消息生产者！";
////        设置消息过期时间
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setExpiration(String.valueOf(5000));
//
//       //    设置发布确认模式
//        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause)->{
//            if (ack) {
//                log.info("消息{}发送成功！",correlationData);
//                MESSAGE_LIST.remove(correlationData.getId());
//            }
//            else {
//             //  消息发送失败 可以进行其他操作
//                log.warn("消息{}发送失败！失败原因：{}",correlationData,cause);
//
//            }
//        });
//
//       //  回退模式
//        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setReturnsCallback(returnedMessage -> {
//            log.info("交换机到队列回退！消息信息{}",returnedMessage.getMessage());
//            Message message1 = returnedMessage.getMessage();
//            MessageProperties messageProperties1 = message1.getMessageProperties();
//            String returnId = messageProperties1.getHeader("spring_returned_message_correlation");
//           log.info("return id=====>{}",returnId);
//           MESSAGE_LIST.remove(returnId);
//            log.error("从交换机到队列出错{}",returnedMessage.getReplyCode());
//        });
//        for (int i = 0; i < 5; i++) {
//            Map<String,Object> order = new HashMap<>();
//            order.put("id",i);
//            order.put("name",message);
//            Message msg = new Message(JSON.toJSONString(order).getBytes(StandardCharsets.UTF_8),messageProperties);
//            String uuid = UUID.randomUUID().toString().replaceAll("-","");
//            CorrelationData cause=new CorrelationData(uuid);
//            MESSAGE_LIST.put(uuid,msg);
//                rabbitTemplate.convertAndSend(RabbitMQConfig.COMMON_EXCHANGE,
//                        "NIUMALL_ALLPAY_ORDER", msg,cause);
//        }
//
//        NavigableSet<String> strings = MESSAGE_LIST.keySet();
//
//        System.out.println("最后队列中的数据---------------");
//        for (String key:strings) {
//            System.out.println(key);
//}
//    }

    public  void producerOder(Order order){
        //      设置消息过期时间
        MessageProperties messageProperties = new MessageProperties();
         // 设置十五分钟支付超时时间
        messageProperties.setExpiration(String.valueOf(1000*60*15));

        //    设置发布确认模式
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause)->{
            if (ack) {
                log.info("消息{}发送成功！",correlationData);
                assert correlationData != null;
                MESSAGE_LIST.remove(correlationData.getId());
            }
            else {
                //  消息发送失败 可以进行其他操作
                log.warn("消息{}发送失败！失败原因：{}",correlationData,cause);

            }
        });

        //  回退模式
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            log.info("交换机到队列回退！消息信息{}",returnedMessage.getMessage());
            Message message1 = returnedMessage.getMessage();
            MessageProperties messageProperties1 = message1.getMessageProperties();
            String returnId = messageProperties1.getHeader("spring_returned_message_correlation");
            log.info("return id=====>{}",returnId);
            MESSAGE_LIST.remove(returnId);
            log.error("从交换机到队列出错{}",returnedMessage.getReplyCode());
        });

        Message msg = new Message(JSON.toJSONBytes(order),messageProperties);

        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        CorrelationData cause=new CorrelationData(uuid);
        //  将消息放入本地HashMap 发送失败时可进行其他操作
        MESSAGE_LIST.put(uuid,msg);

        rabbitTemplate.convertAndSend(RabbitMQConfig.COMMON_EXCHANGE, "NIUMALL_ALLPAY_ORDER", msg,cause);
    }

}
