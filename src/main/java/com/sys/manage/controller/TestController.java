package com.sys.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sys.manage.common.R;
import com.sys.manage.entity.Address;
import com.sys.manage.entity.SysUser;
import com.sys.manage.service.AddressService;
import com.sys.manage.service.SysUserService;
import com.sys.manage.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("list")
public class TestController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AddressService addressService;


    @RequestMapping("allUser")
    @PreAuthorize("hasRole('ROLE_admin')")
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

    @RequestMapping("/address")
    public R getAddress(){
        HashMap<String, Object> resultMap = new HashMap<>();
        List<Address> addresss = addressService.list(new QueryWrapper<Address>().ge("id", 1));
        resultMap.put("addresss",addresss);
        return R.ok(resultMap);
    }

}
