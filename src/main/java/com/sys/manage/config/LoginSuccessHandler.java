package com.sys.manage.config;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sys.manage.common.R;
import com.sys.manage.entity.Address;
import com.sys.manage.entity.SysMenu;
import com.sys.manage.entity.SysRole;
import com.sys.manage.entity.SysUser;
import com.sys.manage.service.AddressService;
import com.sys.manage.service.SysMenuService;
import com.sys.manage.service.SysRoleService;
import com.sys.manage.service.SysUserService;
import com.sys.manage.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        String username = authentication.getName();
        String token = JWTUtil.createJWT(username);
        SysUser currentUser = sysUserService.getByUserName(username);

        List<SysRole> roleList = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id = " + currentUser.getId()));
        String collect = roleList.stream().map(SysRole::getName).collect(Collectors.joining(","));
        currentUser.setRoles(collect);
        Set<SysMenu> sysMenuSet = new HashSet<>();
        for(SysRole sysRole:roleList){
            List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().inSql("id", "select menu_id from sys_role_menu where role_id = "+sysRole.getId()));
            sysMenuSet.addAll(sysMenuList);
        }
        ArrayList<SysMenu> sysMenus = new ArrayList<>(sysMenuSet);
        sysMenus.sort(Comparator.comparing(SysMenu::getOrderNum));
        List<SysMenu> menuList = sysMenuService.buildTreeMenu(sysMenus);
        R put = R.ok("登录成功");
        put.put("authorization", token);
        put.put("currentUser",currentUser);
        put.put("menuList",menuList);
        byte[] bytes = JSONUtil.toJsonStr(put).getBytes();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
