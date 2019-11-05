package com.ddu.shiro_demo.service;

import com.ddu.shiro_demo.bean.Role;

import java.util.Set;

/**
 * @author LinGF
 */
public interface PermissionService {
    Role getRoleByID(String id);
}
