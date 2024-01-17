package com.sys.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sys.manage.common.PageBean;
import com.sys.manage.common.R;
import com.sys.manage.entity.SysRole;
import com.sys.manage.entity.SysUser;
import com.sys.manage.service.SysRoleService;
import com.sys.manage.service.SysUserService;
import com.sys.manage.utils.DateUtil;
import com.sys.manage.utils.StringUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SysRoleService sysRoleService;

    @Value("${avatarImageFilePath}")
    private String avatarImageFilePath;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('system:user:add') || hasAuthority('system:user:edit')")
    public R saveUser(@RequestBody SysUser sysUser){
        if(sysUser.getId()==null||sysUser.getId()==-1){

        }else {
            sysUser.setUpdateTime(new Date());
            sysUserService.updateById(sysUser);
        }
        return R.ok();
    }

    @RequestMapping(value = "/updateUserPwd",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R updateUserPwd(@RequestBody SysUser sysUser){
        SysUser currentUser = sysUserService.getById(sysUser.getId());
        if (bCryptPasswordEncoder.matches(sysUser.getOldPassword(),currentUser.getPassword())) {
            currentUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getNewPassword()));
            currentUser.setUpdateTime(new Date());
            sysUserService.updateById(currentUser);
            return R.ok();
        }else {
            return R.error("旧密码与原密码不匹配");
        }
    }

    @PostMapping("/uploadImage")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Map<String,Object> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (!file.isEmpty()) {
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            String suffixName = null;
            if (originalFilename != null) {
                suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFileName = DateUtil.currentTimeStr() + suffixName;
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(avatarImageFilePath+ newFileName));
            result.put("status", 200);
            result.put("msg", "上传成功！");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("title", newFileName);
            dataMap.put("src", "img/" + newFileName);
            result.put("data", dataMap);
        }
        return result;
    }

    @PostMapping("/updateAvatar")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R updateAvatar(@RequestBody SysUser sysUser){
        SysUser currentUser = sysUserService.getById(sysUser.getId());
        currentUser.setAvatar(sysUser.getAvatar());
        sysUserService.updateById(currentUser);
        return R.ok();
    }

    @PostMapping("/list")
    @PreAuthorize("hasAuthority('system:user:query')")
    public R list(@RequestBody PageBean pageBean){
        Page<SysUser> pageResult = sysUserService.page(new Page<>(pageBean.getPageNum(), pageBean.getPageSize()),new QueryWrapper<SysUser>().like(StringUtil.isNotEmpty(pageBean.getQuery()),"username",pageBean.getQuery()));
        List<SysUser> userList = pageResult.getRecords();
        HashMap<String, Object> resultMap = new HashMap<>();
        userList.forEach(user->user.setRoleList(sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id = " + user.getId())).stream().map(SysRole::getName).collect(Collectors.toList())));
        resultMap.put("userList",userList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    @GetMapping("/updateStatus/{id}/status/{status}")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R updateStatus(@PathVariable("id") Integer id,
                                   @PathVariable("status") Character status){
        SysUser sysUser = sysUserService.getById(id.longValue());
        sysUser.setUpdateTime(new Date());
        sysUser.setStatus(status.toString());
        sysUserService.updateById(sysUser);
        return R.ok();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public R findById(@PathVariable("id") Integer id){
        SysUser sysUser = sysUserService.getById(id.longValue());
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("sysUser",sysUser);
        return R.ok(resultMap);
    }

    @PostMapping("/checkUserName")
    @PreAuthorize("hasAuthority('system:user:query')")
    public R checkUserName(@RequestBody SysUser sysUser){
        if(sysUserService.getByUserName(sysUser.getUsername())!=null){
            return R.error(400,"用户名已存在！");
        }
        return R.ok();
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:user:add') || hasAuthority('system:user:edit')")
    public R insert(@RequestBody SysUser sysUser){
        sysUser.setUpdateTime(new Date());
        sysUser.setCreateTime(new Date());
        sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
        boolean is = sysUserService.save(sysUser);
        if(!is) return R.error(500,"添加失败");
        return R.ok();
    }
}
