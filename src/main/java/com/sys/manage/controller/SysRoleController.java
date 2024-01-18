package com.sys.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sys.manage.common.Constant;
import com.sys.manage.common.PageBean;
import com.sys.manage.common.R;
import com.sys.manage.entity.SysRole;
import com.sys.manage.entity.SysRoleMenu;
import com.sys.manage.entity.SysUserRole;
import com.sys.manage.service.SysRoleMenuService;
import com.sys.manage.service.SysRoleService;
import com.sys.manage.service.SysUserRoleService;
import com.sys.manage.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @GetMapping("/listAll")
    @PreAuthorize("hasAuthority('system:role:query')")
    public R listAll(){
        List<SysRole> roleList = sysRoleService.list();
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("roleList",roleList);
        return R.ok(resultMap);
    }


    @Transactional
    @PostMapping("/grantRole/{id}")
    @PreAuthorize("hasAuthority('system:user:role')")
    public R grantRole(@PathVariable("id") Long userId, @RequestBody Long[] roleIds){
        if (userId == 1L) {
            return R.error(Constant.NO_AUTHORITY);
        }
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id",userId));
        List<SysUserRole> userRoleList = new ArrayList<>();
        Arrays.stream(roleIds).forEach(roleId->
            userRoleList.add(SysUserRole.builder()
                    .userId(userId)
                    .roleId(roleId)
                    .build())
        );
        sysUserRoleService.saveBatch(userRoleList);
        return R.ok();
    }


    @PostMapping("/list")
    @PreAuthorize("hasAuthority('system:user:query')")
    public R list(@RequestBody PageBean pageBean){
        String query = pageBean.getQuery().trim();
        Page<SysRole> pageResult = sysRoleService.page(new Page<>(pageBean.getPageNum(), pageBean.getPageSize()), new QueryWrapper<SysRole>().like(StringUtil.isNotEmpty(query), "name", query));
        List<SysRole> roleList = pageResult.getRecords();
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("roleList",roleList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }


    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:role:add') || hasAuthority('system:role:edit')")
    public R save(@RequestBody SysRole sysRole){
        sysRole.setUpdateTime(new Date());
        sysRoleService.updateById(sysRole);
        return R.ok();
    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:role:add')")
    public R insert(@RequestBody SysRole sysRole){
        sysRole.setCreateTime(new Date());
        sysRole.setUpdateTime(new Date());
        sysRoleService.save(sysRole);
        return R.ok();

    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public R getSysRoleById(@PathVariable("id") Long roleId){
        SysRole sysRole = sysRoleService.getById(roleId);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("sysRoleList",sysRole);
        return R.ok(resultMap);
    }

    @Transactional
    @PreAuthorize("hasAuthority('system:role:delete')")
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        sysRoleService.removeByIds(Arrays.asList(ids));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", (Object) ids));
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id", (Object) ids));
        return R.ok();
    }


}
