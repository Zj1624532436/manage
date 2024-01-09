package com.sys.manage.config;

import com.sys.manage.service.impl.MyUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private MyUserDetailServiceImpl myUserDetailService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final String[] URL_WHITELIST = {
            "/login","/logout","/captcha","/password","/image/**","/test/**","/love-time/**"
    };

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //开启跨域 csrf攻击 关闭
        http.cors().and().csrf().disable()
        //登录登出配置
        .formLogin().successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
//                .and().logout().logoutSuccessHandler()
        //session禁用配置
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //拦截规则配置
                .and().authorizeRequests().antMatchers(URL_WHITELIST).permitAll().anyRequest().authenticated()
        //异常处理配置
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
        //自定义过滤器配置
                .and().addFilter(jwtAuthenticationFilter());
    }
}
