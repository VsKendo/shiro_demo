package com.ddu.shiro_demo.dao;

import com.ddu.shiro_demo.bean.Role;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleDao {

    static List<Role> db;

    static {
        db = new ArrayList<>();
        db.add(new Role("r001","admin"));
        db.add(new Role("r002","guest"));
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<Role> getRoles(){
        return db;
    }

    /**
     * 单条查询
     *
     * @param id 角色id
     * @return
     */
    public Role getOne(String id) {
        for (Role user : db) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}