package com.sys.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.manage.entity.SysMenu;
import com.sys.manage.entity.SysRole;
import com.sys.manage.entity.SysUser;
import com.sys.manage.mapper.SysMenuMapper;
import com.sys.manage.mapper.SysRoleMapper;
import com.sys.manage.service.SysUserService;
import com.sys.manage.mapper.SysUserMapper;
import com.sys.manage.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 51430
* @description 针对表【sys_user】的数据库操作Service实现
* @createDate 2024-01-08 14:03:20
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Override
    public SysUser getByUserName(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId){
        StringBuilder authority = new StringBuilder();
        List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id = " + userId));
        if (!roleList.isEmpty()) {
            String roleCodeStrs = roleList.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
            authority.append(roleCodeStrs);
        }
        HashSet<String> menuCodeSet = new HashSet<>();
        for(SysRole sysRole:roleList){
            List<SysMenu> sysMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().inSql("id", "select menu_id from sys_role_menu where role_id = "+sysRole.getId()));
            for(SysMenu sysMenu:sysMenuList){
                String perms = sysMenu.getPerms();
                if (StringUtil.isNotEmpty(perms)) {
                    menuCodeSet.add(perms);
                }
            }
        }
        if(!menuCodeSet.isEmpty()){
            authority.append(",");
            String menuCodeStrs = String.join(",", menuCodeSet);
            authority.append(menuCodeStrs);
        }
        return authority.toString();
    }

    @Override
    public Boolean checkHasMax(SysUser sysUser){
        return sysUser.getId() == 1L;
    }
}




