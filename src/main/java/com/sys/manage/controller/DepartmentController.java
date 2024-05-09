package com.sys.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sys.manage.common.PageBean;
import com.sys.manage.common.R;
import com.sys.manage.entity.SysDepartment;
import com.sys.manage.entity.SysRoleMenu;
import com.sys.manage.entity.SysUserRole;
import com.sys.manage.service.impl.SysDepartmentServiceImpl;
import com.sys.manage.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/sys/department")
public class DepartmentController {


    @Autowired
    private SysDepartmentServiceImpl sysDepartmentService;

    @PostMapping("list")
    public R getList(@RequestBody PageBean pageBean){
        String query = pageBean.getQuery().trim();
        Page<SysDepartment> page = sysDepartmentService.page(new Page<>(pageBean.getPageNum(), pageBean.getPageSize()), new QueryWrapper<SysDepartment>().like(StringUtil.isNotEmpty(query), "company_name", query));
        List<SysDepartment> list = page.getRecords();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list",list);
        resultMap.put("total",page.getTotal());
        return R.ok(resultMap);
    }

    @GetMapping("/{id}")
    public R getSysRoleById(@PathVariable("id") Long id){
        SysDepartment sysDepartment = sysDepartmentService.getById(id);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("sysDepartmentList",sysDepartment);
        return R.ok(resultMap);
    }

    @PostMapping("/add")
    public R insert(@RequestBody SysDepartment sysDepartment){
        //获取数据库和类名相似的
        SysDepartment companyName = sysDepartmentService.getOne(new QueryWrapper<SysDepartment>().eq("company_name", sysDepartment.getCompanyName()));
        if(companyName!=null){
            return R.error("疑似有相同公司了");
        }
        sysDepartment.setCreateTime(new Date());
        sysDepartmentService.save(sysDepartment);
        return R.ok();
    }

    @PostMapping("/save")
    public R save(@RequestBody SysDepartment sysDepartment){
        sysDepartmentService.updateById(sysDepartment);
        return R.ok();
    }


    @Transactional
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        sysDepartmentService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
