package com.ddu.shiro_demo.dao;


import com.ddu.shiro_demo.bean.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.HashSet;

@Component
public class UserDao {

    static Set<User> db;

    static {
        db = new HashSet<>();
        //秘钥如何生成？ shiroDemoApplication里有
        db.add(new User("u001", "user01", "0099f404345259869d8f0d25158371b0"));
        db.add(new User("u002", "user02", "74408d5e536e1ec2535ba254e2474160"));
        db.add(new User("u003", "user03", "123456"));
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public Set<User> getUsers() {
        return db;
    }

    /**
     * 单条查询
     *
     * @param username 用户id
     * @return
     */
    public User getUser(String username) {
        for (User user : db) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

}
