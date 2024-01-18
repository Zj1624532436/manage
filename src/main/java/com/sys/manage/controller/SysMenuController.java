package com.sys.manage.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sys.manage.common.R;
import com.sys.manage.entity.SysMenu;
import com.sys.manage.entity.SysRoleMenu;
import com.sys.manage.service.SysMenuService;
import com.sys.manage.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统菜单Controller控制器
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @GetMapping("/treeList")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public R treeList(){
        List<SysMenu> menuList = sysMenuService.list();
        List<SysMenu> menuListResult = sysMenuService.buildTreeMenu((ArrayList<SysMenu>) menuList);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("menuList",menuListResult);
        return R.ok(resultMap);
    }

    @GetMapping("/menus/{id}")
    @PreAuthorize("hasAuthority('system:role:menu')")
    public R menus(@PathVariable("id") Integer roleId){
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId));
        List<Long> menuIdList = sysRoleMenus.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("menuIdList",menuIdList);
        return R.ok(resultMap);
    }

    @Transactional
    @PostMapping("/updateMenus/{id}")
    @PreAuthorize("hasAuthority('system:role:menu')")
    public R updateMenus(@PathVariable("id")Long roleId,
                                  @RequestBody Long[] menuIds){

        sysRoleMenuService.removeById(roleId);
        List<SysRoleMenu> roleMenuList = Arrays.stream(menuIds)
                .map(menuId ->
                        SysRoleMenu.builder()
                                .roleId(roleId)
                                .menuId(menuId)
                                .build()
                ).collect(Collectors.toList());
        sysRoleMenuService.saveBatch(roleMenuList);
        return R.ok();
    }



}
