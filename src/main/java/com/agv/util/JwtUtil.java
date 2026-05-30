package com.agv.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // 密钥（实际项目应放在配置文件中）
//    private static final String SECRET = "mySecretKey12345678901234567890";
    private static final String SECRET = "mySecretKey123456789012345678901234567890";
    // token 有效期（7天，单位：毫秒）
    private static final long EXPIRE = 1000 * 60 * 60 * 24 * 7;

    // 生成密钥对象（新版本要求）
    private static Key getKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }



    /**
     * 生成 token
     * @param username 用户名
     * @return token 字符串
     */

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expireDate  = new Date(now.getTime() + EXPIRE);

        return Jwts.builder()
                .setSubject(username)//存放用户
                .claim("role", role)  // ✅ 添加角色信息
                .setIssuedAt(now)//创建时间
                .setExpiration(expireDate)//过期时间
//                .signWith(SignatureAlgorithm.HS512, SECRET)//签名算法
                .signWith(getKey(),SignatureAlgorithm.HS256)//签名算法
                .compact();
    }

    /**
     * 从 token 中提取用户名
     */
    public String extractUsername(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(getKey())
                .parseClaimsJws(token)
                .getBody();
        return body.getSubject();
    }
    public String extractRole(String token) {
        Claims claims  = Jwts.parser()
                .setSigningKey(getKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }



    /**
     * 验证 token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
