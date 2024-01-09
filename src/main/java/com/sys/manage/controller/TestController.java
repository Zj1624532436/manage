package com.sys.manage.controller;

import com.sys.manage.common.R;
import com.sys.manage.entity.SysUser;
import com.sys.manage.service.SysUserService;
import com.sys.manage.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("list")
public class TestController {

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping("allUser")
    public R getAllUserList(){
        HashMap<String, Object> resultMap = new HashMap<>();
        List<SysUser> list = sysUserService.list();
        resultMap.put("useList",list);
        return R.ok(resultMap);
    }
    @RequestMapping("/login")
    public R login(){
        String zj1234 = JWTUtil.createJWT("zj1234");
        return R.ok().put("token",zj1234);
    }

}
