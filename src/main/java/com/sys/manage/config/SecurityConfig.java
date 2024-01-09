package com.sys.manage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] URL_WHITELIST = {
            "/login","/logout","/captcha","/password","/image/**","/test/**"
    };
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //开启跨域 csrf攻击 关闭
        http.cors().and().csrf().disable()
        //登录登出配置
        .formLogin()
//                .successHandler().failureHandler().and().logout().logoutSuccessHandler()
        //session禁用配置
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //拦截规则配置
                .and().authorizeRequests().antMatchers(URL_WHITELIST).permitAll().anyRequest().authenticated();
        //异常处理配置
        //自定义过滤器配置
    }
}
