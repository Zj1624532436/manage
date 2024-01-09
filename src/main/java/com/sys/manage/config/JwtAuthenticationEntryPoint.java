package com.sys.manage.config;

import cn.hutool.json.JSONUtil;
import com.sys.manage.common.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        R error = R.error(HttpServletResponse.SC_UNAUTHORIZED, "认证失败，请登录");
        byte[] bytes = JSONUtil.toJsonStr(error).getBytes();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
