package com.agv.test;

import com.agv.service.AgvService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AgvServiceTest {

    @Autowired
    private AgvService agvService;

    @Test
    public void testCount() {
        Long count = agvService.countAgv(null, null, null, null);
        System.out.println("AGV总数：" + count);
    }
}