package com.sys.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.manage.entity.SysMenu;
import com.sys.manage.service.SysMenuService;
import com.sys.manage.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* @author 51430
* @description 针对表【sys_menu】的数据库操作Service实现
* @createDate 2024-01-08 14:23:30
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

    @Override
    public List<SysMenu> buildTreeMenu(ArrayList<SysMenu> sysMenus) {
        ArrayList<SysMenu> resultMenuList = new ArrayList<>();
        for (SysMenu sysMenu:sysMenus){
            for (SysMenu sysMenu1:sysMenus){
                if(Objects.equals(sysMenu1.getParentId(), sysMenu.getId())){
                    sysMenu.getChildren().add(sysMenu1);
                }
            }
            if(sysMenu.getParentId()==0L){
                resultMenuList.add(sysMenu);
            }
        }
        return resultMenuList;
    }
}




