package com.sys.manage.config;

import cn.hutool.json.JSONUtil;
import com.sys.manage.common.R;
import com.sys.manage.utils.JWTUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        String username = authentication.getName();
        String token = JWTUtil.createJWT(username);
        R put = R.ok("登录成功").put("authorization", token);
        byte[] bytes = JSONUtil.toJsonStr(put).getBytes();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
