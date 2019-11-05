package com.ddu.shiro_demo.controller;

import com.ddu.shiro_demo.utils.CommonResult;
import com.ddu.shiro_demo.bean.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    @ResponseBody
    @RequiresGuest
    public CommonResult login(User user) {//可额外传入值 boolean remember_me 此处懒得写
        Subject subject = SecurityUtils.getSubject();
        //是否已经登录
        if(subject.isAuthenticated()){
            return CommonResult.success("已经登录,请先登出");
        }

        if(user.getName() == null || user.getPassword()==null || user.getName().length()<1 || user.getPassword().length()<1 ){
            return CommonResult.failure("请输入值");
        }
        //添加用户认证信息
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getName(),
                user.getPassword()
        );
        //usernamePasswordToken.setRememberMe(remember_me);//如果传入值，在这里可判断
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
            //subject.checkRole("admin");
            //subject.checkPermissions("query", "add");
        } catch (AuthenticationException e){//直接捕捉它们的父类,防止恶意扫描账户库
            return CommonResult.failure("账户或密码错误");
        }
        //当然还可捕捉更多，比如是否被锁定什么的。此处懒得写

        return CommonResult.success("登入成功");
    }
//    @RequestMapping(value = "/logout" , method = RequestMethod.POST)
//    @ResponseBody
//    public CommonResult logout(){
//        Subject subject = SecurityUtils.getSubject();
//        if(subject.isAuthenticated()){
//            subject.logout();
//            return CommonResult.success("登出成功");
//        }
//        return CommonResult.success("尚未登录");
//    }

    @RequestMapping(value = "/hello")
    public CommonResult hello(){
        return CommonResult.success("hello");
    }

    @RequestMapping(value = "/admin")
    public CommonResult admin() {
        return CommonResult.success("admin");
    }

    @RequestMapping(value = "/aa")
    public CommonResult aa() {
        return CommonResult.success("aa");
    }

}
