package com.ddu.shiro_demo.dao;

import com.ddu.shiro_demo.bean.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testGetUsers(){
        for (User user : userDao.getUsers()) {
            System.out.println(user);
        }
    }

    @Test
    public void testGetOne(){
        String id = "u001";
//        String id = "u006";
     //   System.out.println(userDao.getOne(id));
    }

}
