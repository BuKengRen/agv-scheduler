package com.agv.filter;

import com.agv.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter                                                                {

    @Autowired
    private JwtUtil jwtUtil;

    public void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("=== JWT过滤器被调用，请求路径：" + request.getRequestURI() + "，请求头：" + request.getHeader("Authorization"));

        // 1. 从请求头获取 token
        String token = request.getHeader("Authorization");
        // 2. 如果有 token 并且以 Bearer 开头
        if(token != null && token.startsWith("Bearer ")){
            String substring = token.substring(7);
            System.out.println("=== 提取的 token：" + substring);
            // 3. 验证 token
            if (jwtUtil.validateToken(substring)) {
                // 4. 从 token 中提取用户名
                String username = jwtUtil.extractUsername(substring);
                // 5. 获取角色
                String role = jwtUtil.extractRole(substring);
                System.out.println("=== 从 token 解析出的角色：" + role);
                // 创建权限列表
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority( "ROLE_" + role.toUpperCase()));

                System.out.println("=== 解析出的用户名：" + username);

                // 4. 创建认证对象，放到 SecurityContext 中
//                UserDetails userDetails  = User.withUsername(username).password("")
//                        .authorities(new ArrayList<>()).build();

                UsernamePasswordAuthenticationToken authentication  = new
                        UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("=== 认证成功，已设置到 SecurityContext");

            }else {
                System.out.println("=== token 验证失败");
            }

        }else {
            System.out.println("=== 没有有效的 token");
        }
        // 5. 继续执行后续过滤器
        filterChain.doFilter(request, response);

    }

}
