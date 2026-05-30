package com.agv.service;

import com.agv.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



@Component
public class RabbitMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.AGV_QUEUE)
    public void receiveMessage(String message) {
        log.info("接收到RabbitMQ消息(消费者收到消息)：{}", message);
        // 这里可以写后续业务逻辑，比如：发送邮件、发送短信等
        System.out.println("来到了RabbitMQConsumer下的receiveMessage方法");

    }


}
