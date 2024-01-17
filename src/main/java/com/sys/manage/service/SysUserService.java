package com.sys.manage.service;

import com.sys.manage.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 51430
* @description 针对表【sys_user】的数据库操作Service
* @createDate 2024-01-08 14:03:20
*/
public interface SysUserService extends IService<SysUser> {

    SysUser getByUserName(String username);

    String getUserAuthorityInfo(Long userId);
}
