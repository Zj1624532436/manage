package com.sys.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.manage.entity.Address;
import com.sys.manage.service.AddressService;
import com.sys.manage.mapper.AddressMapper;
import org.springframework.stereotype.Service;

/**
* @author 51430
* @description 针对表【address】的数据库操作Service实现
* @createDate 2024-01-17 12:40:24
*/
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address>
    implements AddressService{

}




