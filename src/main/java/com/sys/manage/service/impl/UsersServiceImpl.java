package com.sys.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.manage.entity.Users;
import com.sys.manage.service.UsersService;
import com.sys.manage.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 51430
* @description 针对表【users】的数据库操作Service实现
* @createDate 2024-01-08 14:27:32
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




