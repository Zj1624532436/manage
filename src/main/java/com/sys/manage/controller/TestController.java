package com.sys.manage.controller;

import com.sys.manage.common.R;
import com.sys.manage.entity.SysUser;
import com.sys.manage.service.SysUserService;
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

}
