package com.ddu.shiro_demo.service.impl;

import com.ddu.shiro_demo.bean.Permission;
import com.ddu.shiro_demo.bean.Role;
import com.ddu.shiro_demo.bean.User;
import com.ddu.shiro_demo.dao.UserDao;
import com.ddu.shiro_demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class LoginServiceImpl implements LoginService {
   @Autowired
    UserDao userDao;

//    public LoginServiceImpl(){
//        userDao=new UserDao();
//    }

    @Override
    public User Login(String username) {
        return userDao.getUser(username);
    }

    /**
     * 模拟数据库查询
     * @param userName
     * @return
     */


}
