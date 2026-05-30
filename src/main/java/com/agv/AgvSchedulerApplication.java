package com.agv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.agv.mapper")
@EnableCaching
@EnableRabbit // ✅ 开启 RabbitMQ
public class AgvSchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgvSchedulerApplication.class, args);
    }
}