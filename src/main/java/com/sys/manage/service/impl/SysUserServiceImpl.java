package com.sys.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.manage.entity.SysUser;
import com.sys.manage.service.SysUserService;
import com.sys.manage.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 51430
* @description 针对表【sys_user】的数据库操作Service实现
* @createDate 2024-01-08 14:03:20
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Override
    public SysUser getByUserName(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username",username));
    }
}




