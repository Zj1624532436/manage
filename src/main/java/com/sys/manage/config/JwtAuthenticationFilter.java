package com.sys.manage.config;

import com.sys.manage.common.JWTConstant;
import com.sys.manage.entity.SysUser;
import com.sys.manage.service.SysUserService;
import com.sys.manage.service.impl.MyUserDetailServiceImpl;
import com.sys.manage.utils.JWTUtil;
import com.sys.manage.utils.StringUtil;
import com.sys.manage.utils.constants.CheckResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//jwt认证过滤器
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    MyUserDetailServiceImpl myUserDetailService;

    private static final String[] URL_WHITELIST = {
            "/login","/logout","/captcha","/password","/image/**","/test/**","/love-time/**"
    };

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        if(StringUtil.isEmpty(token)||new ArrayList<String>(Arrays.asList(URL_WHITELIST)).contains(request.getRequestURI())){
            chain.doFilter(request,response);
            return;
        }
        CheckResult checkResult = JWTUtil.validateJWT(token);
        if(!checkResult.isSuccess()){
            switch (checkResult.getErrCode()){
                case JWTConstant.JWT_ERRCODE_FAIL:throw new JwtException("验证不通过");
                case JWTConstant.JWT_ERRCODE_EXPIRE:throw new JwtException("Token过期");
                case JWTConstant.JWT_ERRCODE_NULL:throw new JwtException("Token不存在");
            }
        }
        Claims claims = JWTUtil.parseJWT(token);
        String username = claims.getSubject();
        SysUser sysUser = sysUserService.getByUserName(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, myUserDetailService.getUserAuthority(sysUser.getId()));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request,response);
    }
}
