package com.hechi.niumall.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //    正常订单消息交换机
    public static final String COMMON_EXCHANGE = "NIUMALL_ALIPAY_OCOMMON_EXCHANGE";
    //    私信交换机
    public static final String DEADEED_EXCHANGE = "NIUMALL_ALIPAY_DEADED_EXCHANGE";
    //    正常消息队列
    public static final String COMMON_QUEUE = "NIUMALL_ALIPAY_COMMOM_QUEUE";
    //    死信队列
    public static final String DEADEED_QUEUE = "NIUMALL_ALIPAY_DEADEED_QUEUE";

    //添加正常订单交换机
    @Bean(COMMON_EXCHANGE)
    public Exchange commonExchange() {
        return ExchangeBuilder
                .directExchange(COMMON_EXCHANGE)
//                持久化
                .durable(true)
                .build();
    }

    //   声明正常消息队列
    @Bean(COMMON_QUEUE)
    public Queue commonQueue() {
        return QueueBuilder.durable(COMMON_QUEUE)
                .deadLetterExchange(DEADEED_EXCHANGE)
                //设置死信队列路由键
                .deadLetterRoutingKey("DEAD_ORDER")
                //设置队列最大长度，超过的将会直接丢进死信队列中
                .maxLength(10000)
                //设置消息存活时间  队列级别60000*15
//                .ttl(500000)
                .build();
    }

    //将正常队列和交换机绑定
    @Bean
    public Binding commonBinding(@Qualifier(COMMON_EXCHANGE) Exchange commonExchange,
                                 @Qualifier(COMMON_QUEUE) Queue commonQueue) {
        return BindingBuilder.bind(commonQueue).to(commonExchange).with("NIUMALL_ALLPAY_ORDER").noargs();
    }

    //    声明死信交换机
    @Bean(DEADEED_EXCHANGE)
    public Exchange deadExchange() {
        return ExchangeBuilder.directExchange(DEADEED_EXCHANGE)
                .durable(true)
                .build();
    }

    //    声明死信队列
    @Bean(DEADEED_QUEUE)
    public Queue deadQueue() {
        return new Queue(DEADEED_QUEUE, true);
    }

    //死信队列和私信交换机绑定
    @Bean
    public Binding deadBinding(@Qualifier(DEADEED_EXCHANGE) Exchange dExchange,
                               @Qualifier(DEADEED_QUEUE) Queue dQueue) {
        return BindingBuilder.bind(dQueue).to(dExchange).with("DEAD_ORDER").noargs();
    }
}
