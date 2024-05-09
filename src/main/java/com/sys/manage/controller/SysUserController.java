package com.sys.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sys.manage.common.Constant;
import com.sys.manage.common.PageBean;
import com.sys.manage.common.R;
import com.sys.manage.entity.Address;
import com.sys.manage.entity.SysRole;
import com.sys.manage.entity.SysUser;
import com.sys.manage.entity.SysUserRole;
import com.sys.manage.service.AddressService;
import com.sys.manage.service.SysRoleService;
import com.sys.manage.service.SysUserRoleService;
import com.sys.manage.service.SysUserService;
import com.sys.manage.utils.DateUtil;
import com.sys.manage.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;


    @Value("${videoFilePath}")
    private String videoFilePath;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @Transactional
    @PreAuthorize("hasAuthority('system:user:add') || hasAuthority('system:user:edit')")
    public R saveUser(@RequestBody SysUser sysUser){
        sysUser.setUpdateTime(new Date());
        if(sysUser.getId()==null||sysUser.getId()==-1){
            sysUser.setCreateTime(new Date());
            sysUser.setPassword(bCryptPasswordEncoder.encode(sysUser.getPassword()));
            boolean is = sysUserService.save(sysUser);
            if(!is) return R.error(500,"添加失败");
        }else {
            if (sysUserService.checkHasMax(sysUser)) {
                return R.error(Constant.NO_AUTHORITY);
            }
            sysUserService.updateById(sysUser);
        }
        return R.ok();
    }

    @RequestMapping(value = "/updateUserPwd",method = RequestMethod.POST)
    @Transactional
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

    @PostMapping(value = "/uploadImage")
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
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(videoFilePath+File.separator+"img"+File.separator+ newFileName));
            result.put("status", 200);
            result.put("msg", "上传成功！");
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("title", newFileName);
            dataMap.put("src", "video/img/" + newFileName);
            result.put("data", dataMap);
        }
        return result;
    }

    @PostMapping("/updateAvatar")
    @Transactional
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
        userList.forEach(user->user.setRoleList(sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id = " + user.getId()))));
        resultMap.put("userList",userList);
        resultMap.put("total",pageResult.getTotal());
        return R.ok(resultMap);
    }

    @GetMapping("/updateStatus/{id}/status/{status}")
    @Transactional
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R updateStatus(@PathVariable("id") Integer id,
                                   @PathVariable("status") Character status){
        SysUser sysUser = sysUserService.getById(id.longValue());
        if (sysUserService.checkHasMax(sysUser)) {
            return R.error(Constant.NO_AUTHORITY);
        }
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

    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public R delete(@RequestBody Long[] ids){
        for (Long id : ids) {
            if (id==1L) {
                return R.error(Constant.NO_AUTHORITY);
            }
        }
        sysUserService.removeByIds(Arrays.asList(ids));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", (Object) ids));
        return R.ok();
    }

    @GetMapping("/resetPassword/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('system:role:menu')")
    public R resetPassword(@PathVariable(value = "id")Integer id){
        SysUser sysUser = sysUserService.getById(id.longValue());
        if (sysUserService.checkHasMax(sysUser)) {
            return R.error(Constant.NO_AUTHORITY);
        }
        sysUser.setPassword(bCryptPasswordEncoder.encode(Constant.DEFAULT_PASSWORD));
        sysUser.setUpdateTime(new Date());
        sysUserService.updateById(sysUser);
        return R.ok();
    }

    @GetMapping("list/address")
    public R getAddress(){
        List<Address> list = addressService.list();
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("address",list);
        return R.ok(resultMap);

    }


    @GetMapping("start")
    public R start() throws InterruptedException, IOException {
            // 创建Process对象
            Process process = Runtime.getRuntime().exec("C:\\Users\\51430\\Desktop\\Clash\\Clash for Windows.exe");

            // 等待程序执行完成
            int exitCode = process.waitFor();

            // 检查程序是否成功执行
            if (exitCode == 0) {
                System.out.println("程序成功执行");
            } else {
                System.out.println("程序执行失败");
            }
        return R.ok();

    }

    @GetMapping("getFileList")
    public R getFileList(@RequestParam(required = false) String url){
        boolean isSpineFile = false;
        String path = videoFilePath;
        if(StringUtil.isNotEmpty(url)){
            path = videoFilePath+File.separator+url;
            isSpineFile = url.contains("spine");
        }
        File resource = new File(path);
        File[] files = resource.listFiles();
        if (files != null) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    if(o1.getName().length() > o2.getName().length()){
                        return 1;
                    }else if(o1.getName().length() < o2.getName().length()){
                        return -1;
                    }
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }else {
            return R.error(url.substring(url.lastIndexOf(".")+1));
        }
        Map<String, Object> result = new HashMap<>();
        ArrayList<String> resultList = new ArrayList<>();
        ArrayList<String> fileNames = new ArrayList<>();
        for(File file: Objects.requireNonNull(files)){
            String name = file.getName();
            if(StringUtil.isNotEmpty(url)){
                if(isSpineFile){
                    if(name.endsWith(".skel")){
                        resultList.add(url+File.separator+name);
                        fileNames.add(name.substring(0,name.lastIndexOf(".")));
                    }
                }else {
                    resultList.add(url+File.separator+name);
                }
            }else {
                resultList.add(name);
            }
        }
        result.put("fileName",fileNames);
        result.put("list",resultList);
        return R.ok(result);
    }
}
