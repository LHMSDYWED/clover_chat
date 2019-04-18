package com.wechat.car.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jiangxin
 * @create 2019-01-15-11:15
 * 拦截器具体拦截的类容
 */
@Component          //将类放入容器需要的注解
public class JwtInterceptor implements HandlerInterceptor{
    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 放行的方法
     * @param request
     * @param response
     * @param handler
     * @return   true就放行  false就不放行
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");
        //无论如何都放行。具体能不能操作还是在具体的操作中去判断。
        //拦截器只是负责把头请求头中包含token的令牌进行一个解析验证。
        //根据之前控制器中方法，来获取Claims，并且把Claims存入请求域中
        //获取请求消息头
        String header = request.getHeader("Authorization");

        if(header!=null && !"".equals(header)){
            //如果有包含有Authorization头信息，就对其进行解析
            if(header.startsWith("Bearer ")){
                //得到token
                String token = header.substring(7);
                //下面这种方式也可以得到token
//                String token = header.split(" ")[1];
                //对令牌进行验证
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    //管理员
                    if(roles!=null && roles.equals("admin")){
                        request.setAttribute("claims_admin", token);
                    }
                    //小区用户
                    if(roles!=null && roles.equals("user")){
                        request.setAttribute("claims_user", token);
                    }
                    //游客
                    if(roles!=null && roles.equals("visitor")){
                        request.setAttribute("claims_visitor", token);
                    }
                }catch (Exception e){
                    throw new RuntimeException("令牌不正确！");
                }
            }
        }
        return true;
    }
}
