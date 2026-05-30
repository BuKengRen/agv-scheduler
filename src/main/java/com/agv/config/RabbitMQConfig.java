package com.agv.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//
//import org.springframework.amqp.core.Queue;
@Configuration
public class RabbitMQConfig {
    public static final String AGV_QUEUE  = "agv_queue";

    @Bean
    public Queue agvQueue(){
        return new Queue(AGV_QUEUE,true);
    }


}
