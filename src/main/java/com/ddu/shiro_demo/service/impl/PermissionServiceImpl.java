package com.ddu.shiro_demo.service.impl;

import com.ddu.shiro_demo.bean.Role;
import com.ddu.shiro_demo.dao.RoleDao;
import com.ddu.shiro_demo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class PermissionServiceImpl implements PermissionService {
    @Autowired
    RoleDao roleDao;

    @Override
    public Role getRoleByID(String id) {
        return roleDao.getOne(id);

    }

}
