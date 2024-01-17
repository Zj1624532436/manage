package com.sys.manage.service.impl;

import com.sys.manage.common.exception.UserCountLockException;
import com.sys.manage.entity.SysUser;
import com.sys.manage.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUserName(username);
        if(sysUser==null){
            throw new UsernameNotFoundException("用户名或密码错误");
        } else if ("1".equals(sysUser.getStatus())) {
            throw new UserCountLockException("该用户账户已被封禁，请联系管理员");
        }
        return new User(sysUser.getUsername(),sysUser.getPassword(),getUserAuthority(sysUser.getId()));
    }

    public List<GrantedAuthority> getUserAuthority(Long userId) {
        String userAuthorityInfo = sysUserService.getUserAuthorityInfo(userId);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userAuthorityInfo);
    }


}
