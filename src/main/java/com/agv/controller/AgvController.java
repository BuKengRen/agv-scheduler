package com.agv.controller;

import com.agv.dto.AgvCreateDTO;
import com.agv.dto.AgvUpdateDTO;
import com.agv.entity.Agv;
import com.agv.service.AgvService;

import com.agv.util.PageResult;
import com.agv.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/agv")
public class AgvController {

    @Autowired
    private AgvService agvService;

    @GetMapping("/list")
    public Result list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer minBattery,
            @RequestParam(required = false) Integer maxBattery
    ) {
        // 计算偏移量（页数从1开始，SQL里OFFSET从0开始）
        int offset = (page - 1) * size;

        // 查询数据
        List<Agv> list = agvService.getAgvList(offset, size, name,status, minBattery, maxBattery);
        // 查询总数
        Long total = agvService.countAgv(name,status, minBattery, maxBattery);

        return Result.success(new PageResult(total, list));
    }



    @PutMapping("/{id}")
    public Result updateStatus(
            @PathVariable Integer id,
            @RequestBody AgvUpdateDTO agvUpdateDTO
            ) {
        System.out.println(agvUpdateDTO.getStatus());
        // 1. 调用 service 更新状态
        agvService.updateStatus(id, agvUpdateDTO);
        // 2. 返回统一响应
        return Result.success();
    }
//    PUT http://localhost:8080/agv/battery/2?battery=80
// 新增 AGV
    @PostMapping
    public Result addAgv(@RequestBody AgvCreateDTO agv) {

        return agvService.addAgv(agv);
    }

    // 删除 AGV
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")// ✅ 只有 ADMIN 角色能删除
    public Result deleteAgv(@PathVariable Integer id) {
        agvService.deleteAgv(id);
        return Result.success();
    }
}