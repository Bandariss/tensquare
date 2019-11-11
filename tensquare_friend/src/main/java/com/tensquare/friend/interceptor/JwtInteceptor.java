package com.tensquare.friend.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInteceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");
        //拦截器只是负责把请求头中包含token的令牌进行解析认证
        String header=request.getHeader("Authorization");

        if(header!=null&&!"".equals(header)){
            //如果包含有头信息，则进行解析
            if(header.startsWith("Bearer ")){
                String token =header.substring(7);
                //验证
                try{
                    Claims claims=jwtUtil.parseJWT(token);
                    String roles=(String)claims.get("roles");
                    if(roles!=null||!roles.equals("admin")){
                        request.setAttribute("claims_admin",claims);
                    }
                    if(roles!=null||!roles.equals("user")){
                        request.setAttribute("claims_user",claims);
                    }
                }
                catch (Exception e){
                    throw new RuntimeException("令牌不正确");
                }
            }
        }
        return true;
    }
}
