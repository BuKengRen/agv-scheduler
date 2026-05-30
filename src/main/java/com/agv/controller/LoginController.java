package com.agv.controller;

import com.agv.dto.LoginRequest;
import com.agv.util.JwtUtil;
import com.agv.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "123456";

    @PostMapping("/login")
//    public Result login(String username, String password) {
    public Result login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // 模拟用户角色（实际应从数据库查询）
        String role = "ADMIN";  // admin 用户是 ADMIN 角色

        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            String s = jwtUtil.generateToken(username,role);
            return Result.success(s);
        }else {
            return Result.badRequest("用户名或密码错误");
        }
    }
}
